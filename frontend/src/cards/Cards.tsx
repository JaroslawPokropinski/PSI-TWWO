import React, { useCallback, useContext, useEffect, useState } from 'react';
import { Button, Input, List } from 'antd';
import { useHistory } from 'react-router-dom';
import { FormattedMessage } from 'react-intl';
import { PAGE_SIZE } from '../configuration/constants';
import Header from '../shared/Header';

import './Cards.css';
import { getSurrogateAvatar } from '../shared/AvatarUtils';
import { Card } from '../dto/Card';
import { PagedResult } from '../shared/PagedResult';
import AuthContext from '../context/AuthContext';
import { LangContext } from '../context/LangContext';
import axios from '../configuration/axios';

import handleHttpError from '../shared/handleHttpError';

function Cards(): JSX.Element {
  const auth = useContext(AuthContext);
  const lang = useContext(LangContext);

  const history = useHistory();
  const [filterText, setFilterText] = useState('');
  const [pageState, setPageState] = useState<PagedResult<Card> | null>(null);
  const onClick = useCallback(
    (card: Card | null) => {
      if (card == null) {
        history.push('/cards/edit');
        return;
      }
      history.push(`/cards/view?state=view&id=${card.id}`);
    },
    [history]
  );

  const changePage = useCallback(
    (page: number, filter: string) => {
      axios
        .get(
          `/api/subject-card/search?page=${page}&size=${PAGE_SIZE}&query=${encodeURIComponent(
            `subjectCode=ke="${filter}" or subjectName=ke="${filter}" or subjectNameInEnglish=ke="${filter}"`
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
    [auth, history]
  );

  useEffect(() => {
    axios
      .get(
        `/api/subject-card/search?page=0&size=${PAGE_SIZE}&query=${encodeURIComponent(
          `subjectCode=ke=""`
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
    changePage(p, filterText);
  }, [filterText, changePage, pageState]);

  return (
    <div className="cards">
      <Header title={lang.getMessage('Subjects cards')} />
      <div>
        <Input
          className="cards-filter"
          placeholder={lang.getMessage('Filter cards')}
          value={filterText}
          onChange={onFilterTextChange}
        />
        <Button
          type="primary"
          className="cards-filter-button"
          onClick={onFilter}
        >
          <FormattedMessage id="Filter" />
        </Button>
      </div>
      {![
        'ROLE_ADMIN',
        'ROLE_COMMISSION_MEMBER',
        'ROLE_SUBJECT_SUPERVISOR',
      ].includes(auth.role) ? null : (
        <Button
          type="primary"
          className="cards-add"
          onClick={() => onClick(null)}
        >
          <FormattedMessage id="Add" />
        </Button>
      )}

      <List
        className="cards-list"
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
            className="cards-item"
            key={item.id}
            onClick={() => onClick(item)}
          >
            <List.Item.Meta
              avatar={
                lang.locale === 'pl'
                  ? getSurrogateAvatar(item.subjectName, 30, 20)
                  : getSurrogateAvatar(item.subjectNameInEnglish, 30, 20)
              }
              title={item.subjectCode}
              description={
                lang.locale === 'pl'
                  ? item.subjectName
                  : item.subjectNameInEnglish
              }
            />
          </List.Item>
        )}
      />
    </div>
  );
}

export default Cards;
