import { Component, ViewChild } from '@angular/core';
import { LoginService } from '../../../services/auth/login.service';
import { WrapComponent } from '../../../components/shared/wrap/wrap.component';
import { CreateManagesFormComponent } from '../../../components/forms/manages-forms/create-manages-form/create-manages-form.component';
import { TableComponent } from '../../../components/shared/table/table.component';
import { CommonModule } from '@angular/common';
import { Manages } from "../../../models/Manages";
import { ManagesService } from '../../../services/manages/manages.service';

@Component({
  selector: 'app-manages-page',
  standalone: true,
  imports: [WrapComponent, CreateManagesFormComponent, TableComponent, CommonModule],
  templateUrl: './manages-page.component.html',
})
export class ManagesPageComponent {
  @ViewChild(CreateManagesFormComponent) createManagesFormComponent!: CreateManagesFormComponent;
  managesData: Manages[] = [];
  filteredManagesData: Manages[] = [];
  selectedRowData: any = null;

  constructor(private managesService: ManagesService, public loginService: LoginService) {};

  ngOnInit(): void {
    this.loadManagesData();
  }

  deleteManages(rowDataFiltered: any): void {
    this.managesService.delete(rowDataFiltered.id).subscribe(() => {
      this.managesData = this.managesData.filter(data => data.id != rowDataFiltered.id);
      this.filteredManagesData = this.filteredManagesData.filter(data => data.id != rowDataFiltered.id);
    })
  }

  loadManagesData(): void {
    this.managesService.findAll().subscribe((data: Manages[]) => {
      this.managesData = data;
      this.filterManagesData();
    }
    );
  }

  addManages(newManages: Manages): void {
    this.managesData.push(newManages);
    this.filterManagesData();
  }

  onAddManage(): void {
    this.createManagesFormComponent.onSubmit();
  }

  filterManagesData(): void {
    this.filteredManagesData = this.managesData.map(manage => ({
      id: manage.id,
      name: manage.facility?.name,
      email: manage.user?.email,
      startDate: manage.startDate,
      endDate: manage.endDate
    }));
  }

}
