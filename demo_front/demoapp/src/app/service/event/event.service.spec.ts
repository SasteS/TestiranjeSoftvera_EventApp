// import { TestBed } from '@angular/core/testing';

// import { EventManagementService } from './event.service';

// describe('EventService', () => {
//   let service: EventManagementService;

//   beforeEach(() => {
//     TestBed.configureTestingModule({});
//     service = TestBed.inject(EventManagementService);
//   });

//   it('should be created', () => {
//     expect(service).toBeTruthy();
//   });
// });


import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { EventManagementService } from './event.service';

describe('EventManagementService', () => {
  let service: EventManagementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(EventManagementService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
