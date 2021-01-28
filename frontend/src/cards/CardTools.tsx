import React from 'react';
import { Button, Divider, Form, Input } from 'antd';
import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons';

const CardTools: React.FunctionComponent<{ modify: boolean }> = ({
  modify = false,
}) => {
  return (
    <Form.List name="usedTeachingTools">
      {(fields, { add, remove }) => (
        <>
          {fields.map((field) => (
            <div key={field.key}>
              <Form.Item
                label="Narzędzie dydaktyczne"
                labelAlign="left"
                name={field.name}
                fieldKey={field.fieldKey}
                rules={[
                  { required: true, message: 'Podaj narzędzie dydaktyczne!' },
                ]}
              >
                <Input
                  placeholder="Podaj narzędzie dydaktyczne"
                  disabled={!modify}
                />
              </Form.Item>

              {modify ? (
                <Button
                  type="dashed"
                  onClick={() => remove(field.name)}
                  icon={<MinusCircleOutlined />}
                >
                  Usuń narzędzie
                </Button>
              ) : null}
              <Divider />
            </div>
          ))}
          {modify ? (
            <Form.Item>
              <Button
                type="dashed"
                onClick={() => add()}
                block
                icon={<PlusOutlined />}
              >
                Dodaj narzędzie dydaktyczne
              </Button>
            </Form.Item>
          ) : null}
        </>
      )}
    </Form.List>
  );
};

export default CardTools;
