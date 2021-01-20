import { Form, Input, InputNumber, Select } from 'antd';
import React, {
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
} from 'react';
import { useHistory, useParams } from 'react-router-dom';
import moment from 'moment';
import axios from '../configuration/axios';
import AuthContext from '../context/AuthContext';
import { LangContext } from '../context/LangContext';
import { FieldOfStudy } from '../dto/FieldOfStudy';
import { OrganisationalUnit } from '../dto/OrganisationalUnit';
import { Program } from '../dto/Program';
import EditorView from '../shared/EditorView';
import handleHttpError from '../shared/handleHttpError';
import useQueryParam from '../shared/useQueryParam';
import { VersionHistory } from '../shared/versionHistory';
import ProgramBlocks from './ProgramBlocks';
import ProgramDisciplines from './ProgramDisciplines';
import ProgramEffects from './ProgramEffects';
import ProgramRequirements from './ProgramRequirements';

import './ProgramsEditor.css';

const ProgramsEditorContent = ({ isArchive = false }) => {
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
      <Form.Item
        className="form-item"
        label="Kod"
        labelAlign="left"
        name="code"
        rules={[{ required: true, message: 'Wprowadź kod programu uczenia!' }]}
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

      {/* <Form.Item
        className="form-item"
        label="Nazwa kierunku"
        labelAlign="left"
        name="field"
        rules={[{ required: true, message: 'Wprowadź nazwe kierunku!' }]}
      >
        <Input placeholder="Wprowadź nazwe kierunku" disabled={!modify} />
      </Form.Item> */}

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
          <Select.Option value="PART_TIME">Studia niestacjonarne</Select.Option>
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
        rules={[{ required: true, message: 'Wprowadź łączną liczbe godzin!' }]}
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
      <ProgramRequirements modify={modify} />
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

      {/* BlokiZajęć */}
      {/* <ProgramBlocks modify={modify} /> */}
      {/* Programowe Efekty Kształcenia */}
      {/* <ProgramEffects modify={modify} /> */}
      {/* Dyscypliny */}
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
            <Select.Option value={d}>{d}</Select.Option>
          ))}
        </Select>
      </Form.Item>
    </>
  );
};

function ProgramsEditor(): JSX.Element {
  const auth = useContext(AuthContext);
  const lang = useContext(LangContext);
  const history = useHistory();

  const [id] = useQueryParam('id');
  const isNew = useMemo(() => id === '', [id]);
  const [program, setProgram] = useState<Program | null>(null);
  const axiosOpts = useMemo(
    () => ({ headers: { Authorization: auth.token } }),
    [auth]
  );

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
          history.goBack();
        })
        .catch((err) => handleHttpError(err, history));
    },
    [axiosOpts, history, program, isNew]
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

  return (
    <div>
      {program == null && !isNew ? null : (
        <EditorView
          name="programs"
          initialVals={program ?? {}}
          onFinish={onFinish}
          queryParams=""
          header="Programy studiów"
          useArchive
          versionHistory={archiveVals}
        >
          <ProgramsEditorContent />
          <ProgramsEditorContent isArchive />
        </EditorView>
      )}
    </div>
  );
}

export default ProgramsEditor;
