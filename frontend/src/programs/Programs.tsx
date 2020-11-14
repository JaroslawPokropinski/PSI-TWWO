import React, { useCallback, useState } from 'react';
import { Button, Input, List } from 'antd';
import { useHistory } from 'react-router-dom';
import Header from '../shared/Header';

import './Programs.css';
import { getSurrogateAvatar } from '../shared/AvatarUtils';

const pageSize = 6;
const mockData = [{ code: 'INZ003854P', name: 'Informatyka stosowana' }];

function Programs(): JSX.Element {
  const history = useHistory();
  const [listData, setListData] = useState(mockData.slice(0, pageSize));
  const onClick = useCallback(
    (effect: string | null) => {
      if (effect == null) {
        history.push('/programs/edit');
        return;
      }
      history.push(`/programs/view?state=update&code=${effect}`);
    },
    [history]
  );
  return (
    <div className="programs">
      <Header title="Programy studiów" />
      <div>
        <Input className="programs-filter" placeholder="Filtruj programy" />
        <Button
          type="primary"
          className="programs-filter-button"
          onClick={() => onClick(null)}
        >
          filtruj
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
            className="programs-item"
            key={item.code}
            onClick={() => onClick(item.code)}
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

export default Programs;
