import React, { useCallback, useContext, useEffect, useState } from 'react';
import { AutoComplete, Button, Card, Form, Select } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { useHistory } from 'react-router-dom';
import axios from '../configuration/axios';
import { PAGE_SIZE } from '../configuration/constants';
import { PagedResult } from '../shared/PagedResult';
import { Effect } from '../dto/Effect';
import AuthContext from '../context/AuthContext';
import handleHttpError from '../shared/handleHttpError';

const CardEffects: React.FunctionComponent<{
  modify: boolean;
  initEffects?: Effect[];
}> = ({ modify = false, initEffects = [] }) => {
  const auth = useContext(AuthContext);
  // const lang = useContext(LangContext);

  const history = useHistory();

  const [chosen, setChosen] = useState<Effect[]>(initEffects);

  const [effects, setEffects] = useState<Effect[] | null>();
  const [value, setValue] = useState('');
  const [disabled, setDisabled] = useState(true);

  useEffect(() => {
    axios
      .get<PagedResult<Effect>>(
        `/api/educational-effects/search?page=0&size=${PAGE_SIZE}&query=${encodeURIComponent(
          `code=ke="${value}" or description=ke="${value}"`
        )}`,
        {
          headers: { Authorization: auth.token },
        }
      )
      .then((res) => {
        setEffects(res.data.results);
      })
      .catch((err) => handleHttpError(err, history));
  }, [auth, history, value]);

  useEffect(() => {
    if (effects == null) return;
    setDisabled(effects.find((e) => e.description === value) == null);
  }, [effects, value]);

  const onAdd = useCallback(
    (add: (e: number) => void) => {
      if (effects == null) return;

      const newElem = effects.find((e) => e.description === value);
      if (newElem == null) return;

      add(newElem.id);

      setChosen([...chosen, newElem]);
    },
    [effects, chosen, value]
  );

  return (
    <>
      {effects == null ? null : (
        <Card title="Efekty kształcenia">
          <Form.List name="educationalEffects">
            {(fields, { add }) => (
              <>
                {modify ? (
                  <Form.Item>
                    <AutoComplete
                      options={effects.map((c) => ({
                        value: c.description,
                        key: c.code,
                      }))}
                      placeholder="Znajdź efekt kształcenia"
                      onChange={(data) => setValue(data)}
                      filterOption={(inputValue, option) => {
                        if (option == null) return true;
                        return (
                          option.value
                            .toUpperCase()
                            .indexOf(inputValue.toUpperCase()) !== -1
                        );
                      }}
                    />
                    <Button
                      icon={<PlusOutlined />}
                      disabled={disabled}
                      onClick={() => onAdd(add)}
                    >
                      Dodaj efekt kształcenia
                    </Button>
                  </Form.Item>
                ) : null}
                {fields.map((field) => (
                  <Form.Item
                    name={field.name}
                    fieldKey={field.fieldKey}
                    key={field.key}
                  >
                    <Select disabled>
                      {chosen.map((e) => (
                        <Select.Option key={e.id} value={e.id}>
                          {e.description}
                        </Select.Option>
                      ))}
                    </Select>
                  </Form.Item>
                ))}
              </>
            )}
          </Form.List>
        </Card>
      )}
    </>
  );
};

export default CardEffects;
