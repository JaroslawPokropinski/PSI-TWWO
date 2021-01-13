interface PagedResult<T> {
  totalSize: number;
  pageSize: number;
  pageNumber: number;

  results: T[];
}

export type { PagedResult };
export default {};
