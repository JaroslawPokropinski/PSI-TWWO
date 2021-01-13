import { AxiosRequestConfig } from 'axios';
import axios from '../configuration/axios';
import { PAGE_SIZE } from '../configuration/constants';
import { PagedResult } from './PagedResult';
import { Versioned } from './Versioned';

export class VersionHistory<T> {
  page: PagedResult<Versioned<T>> | null = null;

  currentRecord = 0;

  current: Versioned<T> | null = null;

  constructor(
    private url: string,
    private id: unknown,
    private axiosOpts: AxiosRequestConfig
  ) {}

  async init(): Promise<void> {
    this.page = (
      await axios.get<PagedResult<Versioned<T>>>(
        `${this.url}/${this.id}?page=0&size=${PAGE_SIZE}`,
        this.axiosOpts
      )
    ).data;
    this.current = this.page.results[this.currentRecord % PAGE_SIZE];
  }

  getCurrent(): Versioned<T> {
    if (this.current == null) throw new Error('History must be initialized!');
    return this.current;
  }

  async getNext(): Promise<Versioned<T>> {
    if (this.page == null || this.current == null)
      throw new Error('History must be initialized!');

    const nextRecord = this.currentRecord + 1;
    if (nextRecord === this.page.totalSize) return this.current;

    if (nextRecord % PAGE_SIZE === 0) {
      const nextPage = nextRecord / PAGE_SIZE;
      this.page = (
        await axios.get<PagedResult<Versioned<T>>>(
          `${this.url}/${this.id}?page=${nextPage}&size=${PAGE_SIZE}`,
          this.axiosOpts
        )
      ).data;
    }
    this.currentRecord = nextRecord;
    this.current = this.page.results[this.currentRecord % PAGE_SIZE];
    return this.current;
  }

  async getPrev(): Promise<Versioned<T>> {
    if (this.page == null || this.current == null)
      throw new Error('History must be initialized!');

    const prevRecord = this.currentRecord - 1;
    if (prevRecord < 0) return this.current;

    if (prevRecord % PAGE_SIZE === PAGE_SIZE - 1) {
      const prevPage = this.page.pageNumber - 1;
      this.page = (
        await axios.get<PagedResult<Versioned<T>>>(
          `${this.url}/${this.id}?page=${prevPage}&size=${PAGE_SIZE}`,
          this.axiosOpts
        )
      ).data;
    }
    this.currentRecord = prevRecord;
    this.current = this.page.results[this.currentRecord % PAGE_SIZE];
    return this.current;
  }

  isFirst(): boolean {
    if (this.page == null || this.current == null)
      throw new Error('History must be initialized!');

    return this.currentRecord === 0;
  }

  isLast(): boolean {
    if (this.page == null || this.current == null)
      throw new Error('History must be initialized!');

    return this.currentRecord === this.page.totalSize - 1;
  }
}
