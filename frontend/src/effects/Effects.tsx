import React, { useCallback, useContext, useEffect, useState } from 'react';
import { Button, Input, List } from 'antd';
import { useHistory } from 'react-router-dom';
import { FormattedMessage } from 'react-intl';
import Header from '../shared/Header';

import './Effects.css';
import { getSurrogateAvatar } from '../shared/AvatarUtils';
import axios from '../configuration/axios';

import handleHttpError from '../shared/handleHttpError';
import AuthContext from '../context/AuthContext';
import { PAGE_SIZE } from '../configuration/constants';
import { PagedResult } from '../shared/PagedResult';
import { Effect } from '../dto/Effect';
import { LangContext } from '../context/LangContext';

function Effects(): JSX.Element {
  const history = useHistory();
  const auth = useContext(AuthContext);
  const lang = useContext(LangContext);

  const [pageState, setPageState] = useState<PagedResult<Effect> | null>(null);

  const onClick = useCallback(
    (effect: Effect | null) => {
      if (effect == null) {
        history.push('/effects/edit');
        return;
      }
      history.push(`/effects/view?state=update&id=${effect.id}`);
    },
    [history]
  );

  const [filterText, setFilterText] = useState('');

  const changePage = useCallback(
    (page: number) => {
      axios
        .get(
          `/api/educational-effects/search?page=${
            page - 1
          }&size=${PAGE_SIZE}&query=${encodeURIComponent(
            `code=ke="${filterText}" or description=ke="${filterText}"`
          )}`,
          {
            headers: { Authorization: auth.token },
          }
        )
        .then((res) => {
          setPageState(res.data);
        })
        .catch((err) => handleHttpError(err, history));
    },
    [filterText, auth, history]
  );

  useEffect(() => {
    axios
      .get(
        `/api/educational-effects/search?page=0&size=${PAGE_SIZE}&query=${encodeURIComponent(
          `code=ke="" or description=ke=""`
        )}`,
        {
          headers: { Authorization: auth.token },
        }
      )
      .then((res) => {
        setPageState(res.data);
      })
      .catch((err) => handleHttpError(err, history));
  }, [auth, history]);

  const onFilterTextChange = useCallback(
    (v: React.ChangeEvent<HTMLInputElement>) => {
      setFilterText(v.currentTarget.value);
    },
    [setFilterText]
  );

  const onFilter = useCallback(() => {
    const p = pageState?.pageNumber ?? 0;
    changePage(p + 1);
  }, [changePage, pageState]);

  return (
    <div className="Effects">
      <Header title={lang.getMessage('Studies effects')} />
      <div>
        <Input
          className="effects-filter"
          placeholder={lang.getMessage('Filter effects')}
          value={filterText}
          onChange={onFilterTextChange}
        />
        <Button
          type="primary"
          className="effects-filter-button"
          onClick={onFilter}
        >
          <FormattedMessage id="Filter" />
        </Button>
      </div>

      <Button
        type="primary"
        className="effects-add"
        onClick={() => onClick(null)}
      >
        <FormattedMessage id="Add" />
      </Button>

      <List
        className="effects-list"
        itemLayout="vertical"
        size="small"
        pagination={{
          position: 'top',
          onChange: (page) => {
            changePage(page);
          },
          pageSize: pageState?.pageSize ?? 0,
          total: pageState?.totalSize ?? 0,
        }}
        dataSource={
          pageState?.results.filter((r) => r.objectState !== 'REMOVED') ?? []
        }
        renderItem={(item) => (
          <List.Item
            className="effects-item"
            key={item.code}
            onClick={() => onClick(item)}
          >
            <List.Item.Meta
              avatar={getSurrogateAvatar(item.code.substring(4, 5), 30, 20)}
              title={item.code}
              description={item.description}
            />
          </List.Item>
        )}
      />
    </div>
  );
}

export default Effects;
