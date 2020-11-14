import React from 'react';
import { AutoComplete, Button, Form, List } from 'antd';
import { PlusOutlined } from '@ant-design/icons';

const mockData = [
  {
    value: 'Dyscyplina 1',
  },
  {
    value: 'Dyscyplina 2',
  },
];

const ProgramDisciplines: React.FunctionComponent<{
  modify: boolean;
}> = ({ modify = false }) => {
  return (
    <>
      Dyscypliny
      {modify ? (
        <Form.Item>
          <AutoComplete
            style={{ width: 800 }}
            options={mockData}
            placeholder="Znajdź dyscyplinę"
            filterOption={(inputValue, option) => {
              if (option == null) return true;
              return (
                option.value.toUpperCase().indexOf(inputValue.toUpperCase()) !==
                -1
              );
            }}
          />
          <Button icon={<PlusOutlined />}>Dodaj dyscyplinę</Button>
        </Form.Item>
      ) : null}
      <Form.Item className="cards-form-item">
        <List
          bordered
          dataSource={['typ ...']}
          renderItem={(item) => <List.Item>{item}</List.Item>}
        />
      </Form.Item>
    </>
  );
};
export default ProgramDisciplines;
