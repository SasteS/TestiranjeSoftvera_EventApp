import 'zone.js';  // Included with Angular CLI.
import 'zone.js/testing';  // Included with Angular CLI.

import { getTestBed } from '@angular/core/testing';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';

// First, initialize the Angular testing environment.
getTestBed().initTestEnvironment(
  BrowserDynamicTestingModule,
  platformBrowserDynamicTesting()
);

// Manually import all the spec files.
//import './app/service/event/event.service.spec';
import './app/registration/registration.component.spec';  // Add more imports as needed
