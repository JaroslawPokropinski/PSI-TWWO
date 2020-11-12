import React from 'react';
import { AutoComplete, Button, Form, List } from 'antd';
import { PlusOutlined } from '@ant-design/icons';

const mockData = [
  {
    value:
      'zna podstawy dotyczące architektury systemu Linux i jego eksploatacji jako serwera lub stacji roboczej użytkownika w systemach informatycznych opartych o platformę Linux',
  },
  {
    value:
      'posiada wiedzę na temat podstaw funkcjonowania systemu Lunux w sieci komputerowej i wykorzystania platformy Linux w budowie infrastruktury sieciowej i usług sieciowych',
  },
];

const ProgramEffects: React.FunctionComponent<{
  modify: boolean;
}> = ({ modify = false }) => {
  return (
    <>
      Programowe Efekty kształcenia
      {modify ? (
        <Form.Item>
          <AutoComplete
            style={{ width: 800 }}
            options={mockData}
            placeholder="Znajdź efekt kształcenia"
            filterOption={(inputValue, option) => {
              if (option == null) return true;
              return (
                option.value.toUpperCase().indexOf(inputValue.toUpperCase()) !==
                -1
              );
            }}
          />
          <Button icon={<PlusOutlined />}>Dodaj efekt kształcenia</Button>
        </Form.Item>
      ) : null}
      <Form.Item className="cards-form-item">
        <List
          bordered
          dataSource={['zna ...', 'potrafi ...']}
          renderItem={(item) => <List.Item>{item}</List.Item>}
        />
      </Form.Item>
    </>
  );
};
export default ProgramEffects;
