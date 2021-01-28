import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons';
import { Button, Divider, Form, InputNumber } from 'antd';
import React, { FunctionComponent, useContext } from 'react';
import { FormattedMessage } from 'react-intl';
import { LangContext } from '../context/LangContext';
import { Card } from '../dto/Card';
import { SubjectCardPicker } from './SubjectCardPicker';

type SemesterEditorProps = {
  modify: boolean;
  initCards: Card[];
};

export const SemesterEditor: FunctionComponent<SemesterEditorProps> = ({
  modify = false,
  initCards = [],
}) => {
  const lang = useContext(LangContext);

  return (
    <Form.List name="semesters">
      {(fields, { add, remove }) => (
        <>
          {fields.map((field) => (
            <div key={field.key}>
              <Form.Item
                label={lang.getMessage('Allowed deficit')}
                labelAlign="left"
                name={[field.name, 'allowedEctsDeficit']}
                fieldKey={[field.fieldKey, 'allowedEctsDeficit']}
                rules={[
                  {
                    required: true,
                    message: lang.getMessage('Set allowed deficit!'),
                  },
                ]}
              >
                <InputNumber min={0} disabled={!modify} />
              </Form.Item>
              <SubjectCardPicker
                modify={modify}
                initCards={initCards}
                field={field}
              />
              {modify ? (
                <Button
                  type="dashed"
                  onClick={() => remove(field.name)}
                  icon={<MinusCircleOutlined />}
                >
                  <FormattedMessage id="Remove semester" />
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
                <FormattedMessage id="Add semester" />
              </Button>
            </Form.Item>
          ) : null}
        </>
      )}
    </Form.List>
  );
};
