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

import './Programs.css';
import { getSurrogateAvatar } from '../shared/AvatarUtils';
import { PagedResult } from '../shared/PagedResult';
import AuthContext from '../context/AuthContext';
import { LangContext } from '../context/LangContext';
import { Program } from '../dto/Program';
import axios from '../configuration/axios';
import handleHttpError from '../shared/handleHttpError';
import { PAGE_SIZE } from '../configuration/constants';

// const mockData = [
//   { code: 'PO-W08-ISTAN-CE--ST-IIM-WRO-/2020', name: 'Informatyka stosowana' },
//   { code: 'PO-W08-ZZZ-PIP--ST-IIM-WRO-/2020', name: 'Zarządzanie' },
// ];

const getPage = (page: number, filter: string, opt: AxiosRequestConfig) =>
  axios.get(
    `/api/studies-program/api/studies-program?page=${page}&size=${PAGE_SIZE}&query=${encodeURIComponent(
      `code=ke="${filter}"`
    )}`,
    opt
  );

function Programs(): JSX.Element {
  const auth = useContext(AuthContext);
  const lang = useContext(LangContext);

  const history = useHistory();
  const [filterText, setFilterText] = useState('');
  const [pageState, setPageState] = useState<PagedResult<Program> | null>(null);
  const axiosOpts = useMemo(
    () => ({ headers: { Authorization: auth.token } }),
    [auth]
  );

  const onClick = useCallback(
    (program: Program | null) => {
      if (program == null) {
        history.push('/programs/edit');
        return;
      }
      history.push(`/programs/view?state=update&id=${program.id}`);
    },
    [history]
  );

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
    <div className="programs">
      <Header title="Programy studiów" />
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

      {!['ROLE_ADMIN', 'ROLE_COMMISSION_MEMBER'].includes(auth.role) ? null : (
        <Button
          type="primary"
          className="programs-add"
          onClick={() => onClick(null)}
        >
          <FormattedMessage id="Add" />
        </Button>
      )}

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

export default Programs;
