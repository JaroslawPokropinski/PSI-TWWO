import React from 'react';
import { Button, Divider, Form, Input } from 'antd';
import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons';

const CardRequirements: React.FunctionComponent<{ modify: boolean }> = ({
  modify = false,
}) => {
  return (
    <Form.List name="requirements">
      {(fields, { add, remove }) => (
        <>
          {fields.map((field) => (
            <>
              <Form.Item
                label="Wymaganie wstępne"
                labelAlign="left"
                name={[field.name, 'desc']}
                fieldKey={[field.fieldKey, 'desc']}
                rules={[
                  { required: true, message: 'Podaj wymaganie wstępne!' },
                ]}
              >
                <Input placeholder="Podaj wymaganie wstępne" />
              </Form.Item>

              {modify ? (
                <Button
                  type="dashed"
                  onClick={() => remove(field.name)}
                  icon={<MinusCircleOutlined />}
                >
                  Usuń wymaganie
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
                Dodaj wymaganie wstępne
              </Button>
            </Form.Item>
          ) : null}
        </>
      )}
    </Form.List>
  );
};

export default CardRequirements;
