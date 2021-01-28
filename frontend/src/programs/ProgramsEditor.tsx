import { Card as AntCard, Form, Input, InputNumber, Select } from 'antd';
import React, {
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
} from 'react';
import { useHistory, useParams } from 'react-router-dom';
import moment from 'moment';
import { useForm } from 'antd/lib/form/Form';
import axios from '../configuration/axios';
import AuthContext from '../context/AuthContext';
import { LangContext } from '../context/LangContext';
import { FieldOfStudy } from '../dto/FieldOfStudy';
import { Program } from '../dto/Program';
import EditorView from '../shared/EditorView';
import handleHttpError from '../shared/handleHttpError';
import useQueryParam from '../shared/useQueryParam';
import { VersionHistory } from '../shared/versionHistory';
import ProgramDisciplines from './ProgramDisciplines';
import ProgramEffects from './ProgramEffects';
import ProgramRequirements from './ProgramRequirements';

import './ProgramsEditor.css';
import { Effect } from '../dto/Effect';
import VerificationStatus from '../shared/VeryficationStatus';

const ProgramsEditorContent = ({
  isArchive = false,
  initEffects = new Array<Effect>(),
}) => {
  const auth = useContext(AuthContext);
  const lang = useContext(LangContext);
  const history = useHistory();

  const { state } = useParams<{ state: string }>();
  const axiosOpts = useMemo(
    () => ({ headers: { Authorization: auth.token } }),
    [auth]
  );
  const modify = useMemo(
    () => (state === 'create' || state === 'edit') && !isArchive,
    [state, isArchive]
  );

  const [fields, setFields] = useState<FieldOfStudy[]>([]);

  useEffect(() => {
    axios
      .get<FieldOfStudy[]>(`/api/field-of-study`, axiosOpts)
      .then((res) => {
        setFields(res.data);
      })
      .catch((err) => handleHttpError(err, history));
  }, [axiosOpts, history]);

  const [selectedDisciplines, setSelectedDisciplines] = useState<number[]>([]);

  return (
    <>
      <AntCard>
        <VerificationStatus modify={modify} label={lang.getMessage('State')} />
        <Form.Item
          className="form-item"
          label="Kod"
          labelAlign="left"
          name="code"
          rules={[
            { required: true, message: 'Wprowadź kod programu uczenia!' },
          ]}
        >
          <Input disabled={!(state === 'edit')} />
        </Form.Item>
        {/* JednostkaOrganizacyjna */}
        <Form.Item
          className="cards-form-item"
          label="Jednostka organizacyjna"
          labelAlign="left"
          name="fieldOfStudyId"
          rules={[
            {
              required: true,
              message: 'Wprowadź jednostkę organizacyjną (wydział)!',
            },
          ]}
        >
          <Select
            placeholder="Wprowadź jednostkę organizacyjną (wydział)"
            disabled={!modify}
          >
            {fields.map((u) => (
              <Select.Option key={u.id} value={u.id}>
                {u.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>
      </AntCard>

      {/* <Form.Item
        className="form-item"
        label="Nazwa kierunku"
        labelAlign="left"
        name="field"
        rules={[{ required: true, message: 'Wprowadź nazwe kierunku!' }]}
      >
        <Input placeholder="Wprowadź nazwe kierunku" disabled={!modify} />
      </Form.Item> */}
      <AntCard>
        <Form.Item
          name="studiesProfile"
          label="Profil studiów"
          labelAlign="left"
          hasFeedback
          rules={[{ required: true, message: 'Wybierz profil studiów!' }]}
        >
          <Select placeholder="Wybierz profil studiów" disabled={!modify}>
            <Select.Option value="PRACTICAL">Praktyczny</Select.Option>
            <Select.Option value="GENERAL_ACADEMIC">
              Ogólnoakademicki
            </Select.Option>
          </Select>
        </Form.Item>
        {/* poziomStudiów */}
        <Form.Item
          name="studiesLevel"
          label="Poziom studiów"
          labelAlign="left"
          hasFeedback
          rules={[{ required: true, message: 'Wybierz poziom studiów!' }]}
        >
          <Select placeholder="Wybierz poziom studiów" disabled={!modify}>
            <Select.Option value="FIRST">Pierwszego stopnia</Select.Option>
            <Select.Option value="SECOND">Drugiego stopnia</Select.Option>
            <Select.Option value="UNIFORM_MAGISTER_STUDIES">
              Jednolite magisterski
            </Select.Option>
          </Select>
        </Form.Item>
        {/* formaStudiów */}
        <Form.Item
          name="studiesForm"
          label="Forma studiów"
          labelAlign="left"
          hasFeedback
          rules={[{ required: true, message: 'Wybierz formę studiów!' }]}
        >
          <Select placeholder="Wybierz formę studiów" disabled={!modify}>
            <Select.Option value="FULL_TIME">Studia stacjonarne</Select.Option>
            <Select.Option value="PART_TIME">
              Studia niestacjonarne
            </Select.Option>
          </Select>
        </Form.Item>

        <Form.Item
          name="degreeTitle"
          label="Tytuł zawodowy"
          labelAlign="left"
          hasFeedback
          rules={[{ required: true, message: 'Wybierz tytuł zawodowy!' }]}
        >
          <Select placeholder="Wybierz tytuł zawodowy" disabled={!modify}>
            <Select.Option value="BACHELOR_OF_SCIENCE">Inżynier</Select.Option>
            <Select.Option value="MASTER_OF_SCIENCE">Magister</Select.Option>
            <Select.Option value="BACHELOR_MASTER_OF_SCIENCE">
              Magister inżynier
            </Select.Option>
          </Select>
        </Form.Item>

        <Form.Item
          name="languageOfStudies"
          label="Język prowadzenia"
          labelAlign="left"
          hasFeedback
          rules={[
            { required: true, message: 'Wybierz język prowadzenia studiów!' },
          ]}
        >
          <Select
            placeholder="Wybierz język prowadzenia studiów"
            disabled={!modify}
          >
            <Select.Option value="polish">Polski</Select.Option>
            <Select.Option value="english">Angielski</Select.Option>
          </Select>
        </Form.Item>

        <Form.Item
          className="form-item"
          label="Liczba semestrów"
          labelAlign="left"
          name="numberOfSemesters"
          rules={[{ required: true, message: 'Wprowadź liczbę semestrów!' }]}
        >
          <InputNumber min={1} disabled={!modify} />
        </Form.Item>

        <Form.Item
          className="form-item"
          label="Łączna liczba godzin (CNPS)"
          labelAlign="left"
          name="totalNumberOfHours"
          rules={[
            { required: true, message: 'Wprowadź łączną liczbe godzin!' },
          ]}
        >
          <InputNumber min={1} disabled={!modify} />
        </Form.Item>

        <Form.Item
          className="form-item"
          label="Liczba punktów ECTS"
          labelAlign="left"
          name="totalNumberOfEctsPoints"
          rules={[{ required: true, message: 'Wprowadź liczbę punktów!' }]}
        >
          <InputNumber min={1} disabled={!modify} />
        </Form.Item>
      </AntCard>
      {/* wymaganiaWstępne */}
      {/* <Form.Item
        className="form-item"
        label="Wymagania wstępne"
        labelAlign="left"
        name="prerequisites"
        rules={[{ required: true, message: 'Wprowadź wymagania wstępne!' }]}
      >
        <Input disabled={!modify} />
      </Form.Item> */}
      <AntCard title={lang.getMessage('Requirements')}>
        <ProgramRequirements modify={modify} />
      </AntCard>
      <AntCard>
        {/* sylwetkaAbsolwenta */}
        <Form.Item
          className="form-item"
          label="Sylwetka absolwenta"
          labelAlign="left"
          name="graduateProfile"
          rules={[{ required: true, message: 'Wprowadź sylwetkę absolwenta!' }]}
        >
          <Input disabled={!modify} />
        </Form.Item>
        {/* możliwośćKontynuacjiStudiów */}
        <Form.Item
          className="form-item"
          label="Możliwość kontynuacji studiów"
          labelAlign="left"
          name="possibilityOfContinuingStudies"
          rules={[
            {
              required: true,
              message: 'Wprowadź możliwość kontynuacji studiów!',
            },
          ]}
        >
          <Input disabled={!modify} />
        </Form.Item>
        {/* związekZMisjąIStrategiąRozwojuUczelni */}
        <Form.Item
          className="form-item"
          label="Związek z misją i strategią rozwoju uczelni"
          labelAlign="left"
          name="connectionWithMissionAndDevelopmentStrategy"
          rules={[
            {
              required: true,
              message: 'Wprowadź związek z misją i strategią rozwoju uczelni!',
            },
          ]}
        >
          <Input disabled={!modify} />
        </Form.Item>
      </AntCard>
      {/* BlokiZajęć */}
      {/* <ProgramBlocks modify={modify} /> */}
      {/* Programowe Efekty Kształcenia */}
      <AntCard title={lang.getMessage('Effects')}>
        <ProgramEffects modify={modify} initEffects={initEffects} />
      </AntCard>
      {/* Dyscypliny */}
      <AntCard title={lang.getMessage('Disciplines')}>
        <ProgramDisciplines
          modify={modify}
          onChange={(ch) => setSelectedDisciplines(ch)}
        />
        <Form.Item
          name="mainDisciplineId"
          label="Główna dyscyplina"
          labelAlign="left"
          hasFeedback
          rules={[{ required: true, message: 'Wybierz główną dyscypline!' }]}
        >
          <Select placeholder="Wybierz główną dyscypline" disabled={!modify}>
            {selectedDisciplines.map((d) => (
              <Select.Option value={d} key={d}>
                {d}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>
      </AntCard>
    </>
  );
};

function ProgramsEditor(): JSX.Element {
  const auth = useContext(AuthContext);
  const history = useHistory();

  const [id] = useQueryParam('id');
  const isNew = useMemo(() => id === '', [id]);
  const [program, setProgram] = useState<Program | null>(null);
  const axiosOpts = useMemo(
    () => ({ headers: { Authorization: auth.token } }),
    [auth]
  );
  const [form] = useForm();

  const onFinish = useCallback(
    (d) => {
      const newProgram = { ...d, inEffectSince: moment.utc().format() };
      (isNew || program == null
        ? axios.post(`/api/studies-program`, [newProgram], axiosOpts)
        : axios.put(
            `/api/studies-program`,
            [{ id: program.id, ...newProgram }],
            axiosOpts
          )
      )
        .then((res) =>
          axios.get<Program[]>(
            `/api/studies-program/${res.data[0].id}`,
            axiosOpts
          )
        )
        .then((res) => {
          setProgram(res.data[0]);
          form.resetFields();
          history.goBack();
        })
        .catch((err) => handleHttpError(err, history));
    },
    [axiosOpts, history, program, isNew, form]
  );

  useEffect(() => {
    if (isNew) return;
    axios
      .get<Program[]>(`/api/studies-program/${id}`, axiosOpts)
      .then((res) => {
        setProgram(res.data[0]);
      })
      .catch((err) => handleHttpError(err, history));
  }, [id, isNew, history, axiosOpts]);

  const [archiveVals, setArchiveVals] = useState<VersionHistory<
    Program
  > | null>(null);

  useEffect(() => {
    if (isNew) return;

    const versionHistory = new VersionHistory<Program>(
      '/api/studies-program/history',
      id,
      { headers: { Authorization: auth.token } }
    );
    versionHistory
      .init()
      .then(() => {
        setArchiveVals(versionHistory);
      })
      .catch((e) => handleHttpError(e, history));
  }, [isNew, id, auth, history]);

  const [effects, setEffects] = useState<Effect[] | null>(null);
  useEffect(() => {
    if (program == null) return;
    if (program.educationalEffects.length === 0) {
      setEffects([]);
      return;
    }

    axios
      .get<Effect[]>(
        `/api/educational-effects/${program.educationalEffects.join(',')}`,
        {
          headers: { Authorization: auth.token },
        }
      )
      .then((res) => {
        setEffects(res.data);
      })
      .catch((e) => handleHttpError(e, history));
  }, [program, auth, history]);

  const onVerify = useCallback(() => {
    axios
      .patch(
        `/api/studies-program/status/${id}`,
        { status: 'VERIFIED' },
        {
          headers: { Authorization: auth.token },
        }
      )
      .then(() => axios.get<Program[]>(`/api/studies-program/${id}`, axiosOpts))
      .then((res) => {
        setProgram(res.data[0]);
        form.resetFields();
      })
      .catch((err) => handleHttpError(err));
  }, [id, auth, form, axiosOpts]);

  const onRemove = useCallback(() => {
    axios.delete(`/api/studies-program/${id}`, {
      headers: { Authorization: auth.token },
    });
  }, [id, auth]);

  return (
    <div>
      {(program == null || effects == null) && !isNew ? null : (
        <EditorView
          form={form}
          name="programs"
          initialVals={program ?? {}}
          onFinish={onFinish}
          queryParams=""
          header="Programy studiów"
          useArchive
          isVerifiable={['ROLE_ADMIN', 'ROLE_COMMISSION_MEMBER'].includes(
            auth.role
          )}
          isAllowedToEdit={['ROLE_ADMIN', 'ROLE_COMMISSION_MEMBER'].includes(
            auth.role
          )}
          isVerified={program?.objectState === 'VERIFIED'}
          versionHistory={archiveVals}
          onVerify={onVerify}
          onRemove={onRemove}
        >
          <ProgramsEditorContent initEffects={effects ?? []} />
          <ProgramsEditorContent isArchive />
        </EditorView>
      )}
    </div>
  );
}

export default ProgramsEditor;
