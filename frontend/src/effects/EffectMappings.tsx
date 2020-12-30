import React from 'react';
import { AutoComplete, Button, Form, List } from 'antd';
import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons';

const mockData = [
  {
    value: 'TWO_01',
  },
  {
    value: 'PSI_05',
  },
];

const EffectMappings: React.FunctionComponent<{ modify: boolean }> = ({
  modify = false,
}) => {
  return (
    <>
      Mapowanie efektu
      {modify ? (
        <Form.Item>
          <AutoComplete
            // style={{ width: 800 }}
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
          dataSource={['PSI_02', 'PSI_03']}
          renderItem={(item) => (
            <List.Item key={item}>
              <List.Item.Meta title={item} />
              {modify ? (
                <Button style={{ borderColor: 'transparent' }}>
                  <MinusCircleOutlined />
                </Button>
              ) : null}
            </List.Item>
          )}
        />
      </Form.Item>
    </>
  );
};

export default EffectMappings;
