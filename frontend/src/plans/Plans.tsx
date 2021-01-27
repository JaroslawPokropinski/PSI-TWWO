import React, {
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
} from 'react';
import { Button, Input, List } from 'antd';
import { useHistory } from 'react-router-dom';
import { AxiosRequestConfig } from 'axios';
import { FormattedMessage } from 'react-intl';
import Header from '../shared/Header';

import './Plans.css';
import { getSurrogateAvatar } from '../shared/AvatarUtils';
import AuthContext from '../context/AuthContext';
import { PagedResult } from '../shared/PagedResult';
import { Plan } from '../dto/Plan';
import handleHttpError from '../shared/handleHttpError';
import { PAGE_SIZE } from '../configuration/constants';
import axios from '../configuration/axios';
import { LangContext } from '../context/LangContext';

// const pageSize = 6;
// const mockData = [
//   {
//     code: 'PO-W08-IST-IO--ST-IIM-WRO-/2020L',
//     name: 'Informatyka stosowana dla cyklu kształcenia od 2020/2021',
//   },
//   {
//     code: 'PO-W08-IST-IS--ST-IIM-WRO-/2020L',
//     name: 'Inżynieria systemów dla cyklu kształcenia od 2020/2021',
//   },
// ];

const getPage = (page: number, filter: string, opt: AxiosRequestConfig) =>
  axios.get(
    `/api/studies-plan/search?page=${page}&size=${PAGE_SIZE}&query=${encodeURIComponent(
      `code=ke="${filter}"`
    )}`,
    opt
  );

function Plans(): JSX.Element {
  const history = useHistory();
  const lang = useContext(LangContext);
  const auth = useContext(AuthContext);

  const [pageState, setPageState] = useState<PagedResult<Plan> | null>(null);
  const axiosOpts = useMemo(
    () => ({ headers: { Authorization: auth.token } }),
    [auth]
  );

  const onClick = useCallback(
    (plan: Plan | null) => {
      if (plan == null) {
        history.push('/plans/edit');
        return;
      }
      history.push(`/plans/view?&id=${plan.id}`);
    },
    [history]
  );

  const [filterText, setFilterText] = useState('');

  const changePage = useCallback(
    (page: number, filter: string) => {
      getPage(page, filter, axiosOpts)
        .then((res) => {
          setPageState(res.data);
        })
        .catch((err) => handleHttpError(err, history));
    },
    [axiosOpts, history]
  );

  useEffect(() => {
    getPage(0, '', axiosOpts)
      .then((res) => {
        setPageState(res.data);
      })
      .catch((err) => handleHttpError(err, history));
  }, [axiosOpts, history]);

  const onFilterTextChange = useCallback(
    (v: React.ChangeEvent<HTMLInputElement>) => {
      setFilterText(v.currentTarget.value);
    },
    [setFilterText]
  );

  const onFilter = useCallback(() => {
    const p = pageState?.pageNumber ?? 0;
    changePage(p, filterText);
  }, [filterText, changePage, pageState]);

  return (
    <div className="plans">
      <Header title="Plany studiów" />
      <div>
        <Input
          className="cards-filter"
          placeholder={lang.getMessage('Filter programs')}
          value={filterText}
          onChange={onFilterTextChange}
        />
        <Button
          type="primary"
          className="programs-filter-button"
          onClick={onFilter}
        >
          <FormattedMessage id="Filter" />
        </Button>
      </div>

      <Button
        type="primary"
        className="programs-add"
        onClick={() => onClick(null)}
      >
        Dodaj
      </Button>

      <List
        className="programs-list"
        itemLayout="vertical"
        size="small"
        pagination={{
          position: 'top',
          onChange: (page) => {
            changePage(page - 1, filterText);
          },
          pageSize: PAGE_SIZE,
          total: pageState?.totalSize ?? 0,
        }}
        dataSource={pageState?.results ?? []}
        renderItem={(item) => (
          <List.Item
            className="programs-item"
            key={item.id}
            onClick={() => onClick(item)}
          >
            <List.Item.Meta
              avatar={getSurrogateAvatar(item.code, 30, 20)}
              title={item.code}
              description={item.code}
            />
          </List.Item>
        )}
      />
    </div>
  );
}

export default Plans;
