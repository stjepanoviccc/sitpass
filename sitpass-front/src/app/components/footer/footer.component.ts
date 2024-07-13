import { Component } from '@angular/core';
import { WrapComponent } from '../shared/wrap/wrap.component';
import { SocialsComponent } from '../shared/socials/socials.component';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [WrapComponent, SocialsComponent],
  templateUrl: './footer.component.html',
})
export class FooterComponent {

}
