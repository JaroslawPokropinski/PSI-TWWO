import React from 'react';
import { AutoComplete, Button, Form, List } from 'antd';
import { PlusOutlined } from '@ant-design/icons';

const mockData = [
  {
    value: 'Grafika komputerowa',
  },
];

const ProgramBlocks: React.FunctionComponent<{
  modify: boolean;
}> = ({ modify = false }) => {
  return (
    <>
      Bloki zajęć
      {modify ? (
        <Form.Item>
          <AutoComplete
            style={{ width: 800 }}
            options={mockData}
            placeholder="Znajdź bloki zajęć"
            filterOption={(inputValue, option) => {
              if (option == null) return true;
              return (
                option.value.toUpperCase().indexOf(inputValue.toUpperCase()) !==
                -1
              );
            }}
          />
          <Button icon={<PlusOutlined />}>Dodaj blok zajęciowy</Button>
        </Form.Item>
      ) : null}
      <Form.Item className="form-item">
        <List
          bordered
          dataSource={['Bazy danych', 'potrafi ...']}
          renderItem={(item) => <List.Item>{item}</List.Item>}
        />
      </Form.Item>
    </>
  );
};

export default ProgramBlocks;
