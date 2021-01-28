export type Program = {
  code: string;
  connectionWithMissionAndDevelopmentStrategy: string;
  degreeTitle:
    | 'BACHELOR_OF_SCIENCE'
    | 'MASTER_OF_SCIENCE'
    | 'BACHELOR_MASTER_OF_SCIENCE';
  disciplinesIds: number[];
  fieldOfStudyId: number;
  graduateProfile: string;
  id: number;
  inEffectSince: string;
  languageOfStudies: string;
  mainDisciplineId: number;
  numberOfSemesters: number;
  possibilityOfContinuingStudies: string;
  prerequisites: string[];
  studiesForm: 'FULL_TIME' | 'PART_TIME';
  studiesLevel: 'FIRST' | 'SECOND' | 'UNIFORM_MAGISTER_STUDIES';
  studiesProfile: 'PRACTICAL' | 'GENERAL_ACADEMIC';
  totalNumberOfEctsPoints: number;
  totalNumberOfHours: number;
  educationalEffects: number[];
  objectState?: 'UNVERIFIED' | 'VERIFIED';
};

export type Program2 = {
  // code: string;
  // connectionWithMissionAndDevelopmentStrategy: string;
  // degreeTitle:
  //   | 'BACHELOR_OF_SCIENCE'
  //   | 'MASTER_OF_SCIENCE'
  //   | 'BACHELOR_MASTER_OF_SCIENCE';
  disciplinesIds: number[];
  // fieldOfStudyId: number;
  // graduateProfile: string;
  // id: number;
  inEffectSince: string;
  // languageOfStudies: string;
  mainDisciplineId: number;
  // numberOfSemesters: number;
  // possibilityOfContinuingStudies: string;
  // prerequisites: string[];
  // studiesForm: 'FULL_TIME' | 'PART_TIME';
  // studiesLevel: 'FIRST' | 'SECOND' | 'UNIFORM_MAGISTER_STUDIES';
  // studiesProfile: 'PRACTICAL' | 'GENERAL_ACADEMIC';
  // totalNumberOfEctsPoints: number;
  // totalNumberOfHours: number;
};
