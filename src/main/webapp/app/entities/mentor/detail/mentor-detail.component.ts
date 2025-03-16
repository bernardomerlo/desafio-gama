import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IMentor } from '../mentor.model';

@Component({
  selector: 'jhi-mentor-detail',
  templateUrl: './mentor-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class MentorDetailComponent {
  mentor = input<IMentor | null>(null);

  previousState(): void {
    window.history.back();
  }
}
