import { Client } from './client.model';

describe('Client', () => {
  it('should create an instance', () => {
    expect(new Client(1, '', '', '')).toBeTruthy();
  });
});
