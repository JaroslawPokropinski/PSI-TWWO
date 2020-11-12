import React from 'react';
import { Button, Divider, Form, Input } from 'antd';
import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons';

const CardGoals: React.FunctionComponent<{ modify: boolean }> = ({
  modify = false,
}) => {
  return (
    <Form.List name="descriptions">
      {(fields, { add, remove }) => (
        <>
          {fields.map((field) => (
            <>
              <Form.Item
                label="Cel przedmiotu"
                labelAlign="left"
                name={[field.name, 'pol']}
                fieldKey={[field.fieldKey, 'pol']}
                rules={[{ required: true, message: 'Podaj cel!' }]}
              >
                <Input placeholder="Podaj cel przedmiotu" disabled={!modify} />
              </Form.Item>

              {modify ? (
                <Button
                  type="dashed"
                  onClick={() => remove(field.name)}
                  icon={<MinusCircleOutlined />}
                >
                  Usuń opis wyżej
                </Button>
              ) : null}
              <Divider />
            </>
          ))}
          {modify ? (
            <Form.Item>
              <Button
                type="dashed"
                onClick={() => add()}
                block
                icon={<PlusOutlined />}
              >
                Dodaj cel przedmiotu
              </Button>
            </Form.Item>
          ) : null}
        </>
      )}
    </Form.List>
  );
};

export default CardGoals;
