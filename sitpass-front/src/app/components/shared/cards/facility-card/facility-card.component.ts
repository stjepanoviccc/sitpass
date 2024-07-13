import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Facility } from '../../../../models/Facility';
import { ButtonComponent } from '../../button/button.component';
import { FileService } from '../../../../services/file/file.service';

@Component({
  selector: 'app-facility-card',
  standalone: true,
  imports: [ButtonComponent, RouterLink],
  templateUrl: './facility-card.component.html',
})
export class FacilityCardComponent {
  @Input() facility!: Facility;
  @Input() extendClass?: string;
  imageUrl: string = '';

  constructor(private fileService: FileService) {}

  ngOnInit() {
    if(this.facility.images) {
      this.facility.images.some((img, index) => {
        this.fileService.getFileContent(img.path).subscribe((imageUrl: string) => {  this.imageUrl = imageUrl; });
        return index === 0;
      });
    }
  }

}
