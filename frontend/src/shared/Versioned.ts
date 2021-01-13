interface Versioned<T> {
  revisionId: number;
  revisionInstant: string;
  revisionType: 'INSERT';

  entity: T;
}

export type { Versioned };
export default {};
