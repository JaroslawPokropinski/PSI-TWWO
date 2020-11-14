import React, { useCallback, useState } from 'react';
import { Button, Input, List } from 'antd';
import { useHistory } from 'react-router-dom';
import Header from '../shared/Header';

import './Cards.css';
import { getSurrogateAvatar } from '../shared/AvatarUtils';

const pageSize = 6;
const mockData = [
  { name: 'Bazy danych', code: 'INZ005234' },
  { name: 'Technologie wsp.wytw.oprogr', code: 'INZ005234' },
  { name: 'Projektowanie sys. informat', code: 'INZ005734' },
  { name: 'Podstawy biz. i ochr.wł.intel', code: 'INZ009204' },
  { name: 'Bezpieczeństwo sys.web.i mob', code: 'INZ002231' },
];

function Cards(): JSX.Element {
  const history = useHistory();
  const [listData, setListData] = useState(mockData.slice(0, pageSize));
  const onClick = useCallback(
    (cards: string | null) => {
      if (cards == null) {
        history.push('/cards/edit');
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
            <List.Item.Meta
              avatar={getSurrogateAvatar(item.name, 30, 20)}
              title={item.code}
              description={item.name}
            />
          </List.Item>
        )}
      />
    </div>
  );
}

export default Cards;
