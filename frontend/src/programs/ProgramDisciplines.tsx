import React, { useContext, useEffect, useMemo, useState } from 'react';
import { AutoComplete, Button, Form, List } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { useHistory } from 'react-router-dom';
import axios from '../configuration/axios';
import { PAGE_SIZE } from '../configuration/constants';
import AuthContext from '../context/AuthContext';
import handleHttpError from '../shared/handleHttpError';
import { Discipline } from '../dto/Discipline';
import PagedPickTable from '../shared/PagedPickTable';

const ProgramDisciplines: React.FunctionComponent<{
  modify: boolean;
  onChange?: (arg: number[]) => void;
}> = ({ modify = false, onChange = undefined }) => {
  const auth = useContext(AuthContext);
  const history = useHistory();
  const axiosOpts = useMemo(
    () => ({ headers: { Authorization: auth.token } }),
    [auth]
  );

  const [disciplines, setDisciplines] = useState<Discipline[]>([]);

  useEffect(() => {
    axios
      .get<Discipline[]>(`/api/disciplines`, axiosOpts)
      .then((res) => {
        setDisciplines(res.data);
      })
      .catch((err) => handleHttpError(err, history));
  }, [axiosOpts, history]);
  const [page, setPage] = useState(0);

  return (
    <>
      <Form.Item name="disciplinesIds">
        <PagedPickTable
          onChange={onChange}
          initVals={[]}
          dataSource={disciplines
            .slice(page * PAGE_SIZE, (page + 1) * PAGE_SIZE)
            .map((d) => ({ id: d.id, value: d.name }))}
          modify={modify}
          changePage={(p) => setPage(p)}
        />
      </Form.Item>
      {/* Dyscypliny
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
      </Form.Item> */}
    </>
  );
};
export default ProgramDisciplines;
