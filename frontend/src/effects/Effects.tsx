import React, { useCallback, useState } from 'react';
import { Button, Input, List } from 'antd';
import { useHistory } from 'react-router-dom';
import Header from '../shared/Header';

import './Effects.css';

const pageSize = 6;
const mockData = [
  {
    code: 'PEK_W01',
    description:
      'zna podstawy dotyczące architektury systemu Linux i jego eksploatacji jako serwera lub stacji roboczej użytkownika w systemach informatycznych opartych o platformę Linux',
  },
  {
    code: 'PEK_W02',
    description:
      'posiada wiedzę na temat podstaw funkcjonowania systemu Lunux w sieci komputerowej i wykorzystania platformy Linux w budowie infrastruktury sieciowej i usług sieciowych',
  },
  {
    code: 'PEK_W03',
    description:
      'posiada wiedzę na temat podstaw funkcjonowania systemu Lunux w sieci komputerowej i wykorzystania platformy Linux w budowie infrastruktury sieciowej i usług sieciowych',
  },
  {
    code: 'PEK_W04',
    description:
      'posiada wiedzę na temat podstaw funkcjonowania systemu Lunux w sieci komputerowej i wykorzystania platformy Linux w budowie infrastruktury sieciowej i usług sieciowych',
  },
  {
    code: 'PEK_W05',
    description:
      'posiada wiedzę na temat podstaw funkcjonowania systemu Lunux w sieci komputerowej i wykorzystania platformy Linux w budowie infrastruktury sieciowej i usług sieciowych',
  },
  {
    code: 'PEK_W06',
    description:
      'posiada wiedzę na temat podstaw funkcjonowania systemu Lunux w sieci komputerowej i wykorzystania platformy Linux w budowie infrastruktury sieciowej i usług sieciowych',
  },
  {
    code: 'PEK_W07',
    description:
      'posiada wiedzę na temat podstaw funkcjonowania systemu Lunux w sieci komputerowej i wykorzystania platformy Linux w budowie infrastruktury sieciowej i usług sieciowych',
  },
];

function Effects(): JSX.Element {
  const history = useHistory();
  const [listData, setListData] = useState(mockData.slice(0, pageSize));
  const onClick = useCallback(
    (effect: string | null) => {
      if (effect == null) {
        history.push('/effects/view?state=create');
        return;
      }
      history.push(`/effects/view?state=update&code=${effect}`);
    },
    [history]
  );
  return (
    <div className="Effects">
      <Header title="Efekty kształcenia" />
      <div>
        <Input className="effects-filter" placeholder="Filtruj efekty" />
        <Button
          type="primary"
          className="effects-filter-button"
          onClick={() => onClick(null)}
        >
          filtruj
        </Button>
      </div>

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
            className="effects-item"
            key={item.code}
            onClick={() => onClick(item.code)}
          >
            <div>{item.description}</div>
          </List.Item>
        )}
      />
    </div>
  );
}

export default Effects;
