import React from 'react';
import { Button, Card, Divider, Form, Input } from 'antd';
import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons';

const CardSecondaryLiterature: React.FunctionComponent<{ modify: boolean }> = ({
  modify = false,
}) => {
  return (
    <Card title="Literatura dodatkowa">
      <Form.List name="secondaryLiterature">
        {(fields, { add, remove }) => (
          <>
            {fields.map((field) => (
              <div key={field.key}>
                <Form.Item
                  label="Dodatkowa pozycja literaturowa"
                  labelAlign="left"
                  name={field.name}
                  fieldKey={field.fieldKey}
                  rules={[{ required: true, message: 'Podaj odniesienie!' }]}
                >
                  <Input placeholder="Podaj odniesienie" disabled={!modify} />
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
                  Dodaj pozycję literaturową
                </Button>
              </Form.Item>
            ) : null}
          </>
        )}
      </Form.List>
    </Card>
  );
};

export default CardSecondaryLiterature;
