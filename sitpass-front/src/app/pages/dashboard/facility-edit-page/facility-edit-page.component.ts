import { Component } from '@angular/core';
import { Location } from '@angular/common';
import { WrapComponent } from '../../../components/shared/wrap/wrap.component';
import { EditFacilityFormComponent } from '../../../components/forms/facility-forms/edit-facility-form/edit-facility-form.component';

@Component({
  selector: 'app-facility-edit-page',
  standalone: true,
  imports: [WrapComponent, EditFacilityFormComponent],
  templateUrl: './facility-edit-page.component.html',
})
export class FacilityEditPageComponent {
  constructor(private location: Location) {}
    
  goBack(): void {
    this.location.back();
  }
}
