import React, { useCallback, useState } from 'react';
import { Button, Input, List } from 'antd';
import { useHistory } from 'react-router-dom';
import Header from '../shared/Header';

import './Effects.css';
import { getSurrogateAvatar } from '../shared/AvatarUtils';

const pageSize = 6;
const mockData = [
  {
    code: 'PEK_W01',
    description:
      'Zna podstawy dotyczące architektury systemu Linux i jego eksploatacji jako serwera lub stacji roboczej użytkownika w systemach informatycznych opartych o platformę Linux',
  },
  {
    code: 'PEK_W02',
    description:
      'Posiada wiedzę na temat podstaw funkcjonowania systemu Lunux w sieci komputerowej i wykorzystania platformy Linux w budowie infrastruktury sieciowej i usług sieciowych',
  },
  {
    code: 'PEK_W03',
    description:
      'Posiada podstawową wiedzę na temat konfiguracji systemu Linux zuwzględnieniem aspektów bezpieczeństwa.',
  },
  {
    code: 'PEK_U01',
    description:
      'Potrafi wykonać podstawowe czynności administracyjne związane z instalacją',
  },
  {
    code: 'PEK_U02',
    description:
      'Potrafi skonfigurować podstawowe elementy podsystemu sieciowego platformy Linux oraz uruchamiać na niej usługi sieciowe',
  },
  {
    code: 'PEK_U03',
    description:
      'Potrafi, w podstawowym zakresie, zabezpieczyć system operacyjny Linux.',
  },
  {
    code: 'PEK_K01',
    description: 'Umie zespołowo pracować nad rozwiązaniem problemów.',
  },
];

function Effects(): JSX.Element {
  const history = useHistory();
  const [listData, setListData] = useState(mockData.slice(0, pageSize));
  const onClick = useCallback(
    (effect: string | null) => {
      if (effect == null) {
        history.push('/effects/edit');
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
