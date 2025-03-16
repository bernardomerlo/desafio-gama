import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMentor } from '../mentor.model';
import { MentorService } from '../service/mentor.service';

@Component({
  templateUrl: './mentor-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MentorDeleteDialogComponent {
  mentor?: IMentor;

  protected mentorService = inject(MentorService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mentorService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
