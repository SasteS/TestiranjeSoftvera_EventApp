import { TestBed } from '@angular/core/testing';

import { EventManagementService } from './event.service';

describe('EventService', () => {
  let service: EventManagementService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EventManagementService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
