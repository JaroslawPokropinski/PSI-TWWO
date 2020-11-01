import React, { useCallback, useState } from 'react';
import { Button, List } from 'antd';
import { useHistory } from 'react-router-dom';
import Header from '../shared/Header';

import './Effects.css';

const mockData = [
  'foreigner',
  'rumor',
  'state',
  'profit',
  'cheese',
  'voter',
  'foreigner',
  'rumor',
  'state',
  'profit',
  'cheese',
  'voter',
  'foreigner',
  'rumor',
  'state',
  'profit',
  'cheese',
  'voter',
];

function Effects(): JSX.Element {
  const history = useHistory();
  const [listData, setListData] = useState(mockData.slice(0, 10));
  const onClick = useCallback(
    (effect: string | null) => {
      if (effect == null) {
        history.push('/effects/edit?state=create');
        return;
      }
      history.push(`/effects/edit?state=update&code=${effect}`);
    },
    [history]
  );
  return (
    <div className="Effects">
      <Header title="Efekty kształcenia" />
      <Button
        type="primary"
        className="effects-add"
        onClick={() => onClick(null)}
      >
        Dodaj
      </Button>

      <List
        className="effects-list"
        itemLayout="vertical"
        size="small"
        pagination={{
          position: 'top',
          onChange: (page) => {
            setListData(mockData.slice(page * 10 - 10, page * 10));
          },
          pageSize: 10,
          total: mockData.length,
        }}
        dataSource={listData}
        renderItem={(item, id) => (
          <List.Item
            className="effects-item"
            key={item + id}
            onClick={() => onClick(item)}
          >
            <div>{item}</div>
          </List.Item>
        )}
      />
    </div>
  );
}

export default Effects;
