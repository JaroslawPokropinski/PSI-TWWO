import React, { useCallback, useState } from 'react';
import { Button, Input, List } from 'antd';
import { useHistory } from 'react-router-dom';
import Header from '../shared/Header';

import './Cards.css';

const pageSize = 6;
const mockData = [
  { name: 'Bazy danych' },
  { name: 'Technologie wsp.wytw.oprogr' },
  { name: 'Projektowanie sys. informat' },
  { name: 'Podstawy biz. i ochr.wł.intel' },
  { name: 'Bezpieczeństwo sys.web.i mob' },
];

function Cards(): JSX.Element {
  const history = useHistory();
  const [listData, setListData] = useState(mockData.slice(0, pageSize));
  const onClick = useCallback(
    (cards: string | null) => {
      if (cards == null) {
        history.push('/cards/view?state=create');
        return;
      }
      history.push(`/cards/view?state=view&name=${encodeURIComponent(cards)}`);
    },
    [history]
  );
  return (
    <div className="cards">
      <Header title="Karty przedmiotów" />
      <div>
        <Input className="cards-filter" placeholder="Filtruj programy" />
        <Button
          type="primary"
          className="cards-filter-button"
          onClick={() => onClick(null)}
        >
          filtruj
        </Button>
      </div>

      <Button
        type="primary"
        className="cards-add"
        onClick={() => onClick(null)}
      >
        Dodaj
      </Button>

      <List
        className="cards-list"
        itemLayout="vertical"
        size="small"
        pagination={{
          position: 'top',
          onChange: (page) => {
            setListData(
              mockData.slice(page * pageSize - pageSize, page * pageSize)
            );
          },
          pageSize,
          total: mockData.length,
        }}
        dataSource={listData}
        renderItem={(item) => (
          <List.Item
            className="cards-item"
            key={item.name}
            onClick={() => onClick(item.name)}
          >
            <div>{item.name}</div>
          </List.Item>
        )}
      />
    </div>
  );
}

export default Cards;
