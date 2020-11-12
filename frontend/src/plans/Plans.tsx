import React, { useCallback, useState } from 'react';
import { Button, Input, List } from 'antd';
import { useHistory } from 'react-router-dom';
import Header from '../shared/Header';

import './Plans.css';

const pageSize = 6;
const mockData = [
  {
    code: 'INZ003854P',
    name: 'Informatyka stosowana dla cyklu kształcenia od 2020/2021',
  },
];

function Plans(): JSX.Element {
  const history = useHistory();
  const [listData, setListData] = useState(mockData.slice(0, pageSize));
  const onClick = useCallback(
    (effect: string | null) => {
      if (effect == null) {
        history.push('/plans/edit');
        return;
      }
      history.push(`/plans/view?state=update&code=${effect}`);
    },
    [history]
  );
  return (
    <div className="plans">
      <Header title="Plany studiów" />
      <div>
        <Input className="plans-filter" placeholder="Filtruj plany" />
        <Button
          type="primary"
          className="plans-filter-button"
          onClick={() => onClick(null)}
        >
          Filtruj
        </Button>
      </div>

      <Button
        type="primary"
        className="plans-add"
        onClick={() => onClick(null)}
      >
        Dodaj
      </Button>

      <List
        className="plans-list"
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
            className="plans-item"
            key={item.code}
            onClick={() => onClick(item.code)}
          >
            <div>{item.name}</div>
          </List.Item>
        )}
      />
    </div>
  );
}

export default Plans;
