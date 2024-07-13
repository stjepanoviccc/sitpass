import { Component, ElementRef, ViewChild } from '@angular/core';
import { Chart } from 'chart.js/auto';
import { ActivatedRoute, Router } from '@angular/router';
import { WrapComponent } from '../../../components/shared/wrap/wrap.component';
import { ButtonComponent } from '../../../components/shared/button/button.component';
import { FacilityService } from '../../../services/facility/facility.service';
import { AnalyticsService } from '../../../services/analytics/analytics.service';
import { ManagesService } from '../../../services/manages/manages.service';
import { Facility } from '../../../models/Facility';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-facility-analytics-page',
  standalone: true,
  imports: [WrapComponent, ButtonComponent],
  templateUrl: './facility-analytics-page.component.html',
})
export class FacilityAnalyticsPageComponent {
  facilityId!: number;
  isUserManager: Boolean = false;

  private chartInstances: Chart[] = [];

  // custom forms fields
  @ViewChild('usersCustomDateFrom') usersCustomDateFrom!: ElementRef;
  @ViewChild('usersCustomDateTo') usersCustomDateTo!: ElementRef;
  @ViewChild('reviewsCustomDateFrom') reviewsCustomDateFrom!: ElementRef;
  @ViewChild('reviewsCustomDateTo') reviewsCustomDateTo!: ElementRef;
  @ViewChild('peakHoursCustomDateFrom') peakHoursCustomDateFrom!: ElementRef;
  @ViewChild('peakHoursCustomDateTo') peakHoursCustomDateTo!: ElementRef;

  // analytics info
  usersCountByWeek!: number | undefined;
  usersCountByMonth!: number | undefined;
  usersCountByYear!: number | undefined;
  reviewsCountByWeek!: number | undefined;
  reviewsCountByMonth!: number | undefined;
  reviewsCountByYear!: number | undefined;
  peakHoursByDay!: any;
  peakHoursByWeek!: any;
  peakHoursByMonth!: any;

  usersCountByCustomDate!: number | undefined;
  reviewsCountByCustomDate!: number | undefined;
  peakHoursByCustomDate!: any;

  ALL: string = 'ALL'
  DAY: string = 'DAY';
  WEEK: string = 'WEEK';
  MONTH: string = 'MONTH';
  YEAR: string = 'YEAR';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private facilityService: FacilityService,
    private managesService: ManagesService,
    private analyticsService: AnalyticsService
  ) {}

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.facilityId = +params['id'];
    });

    this.loadData().then(() => {
      this.createCharts();
    });

    window.addEventListener('resize', () => this.createCharts());
  }

  ngOnDestroy(): void {
    this.destroyCharts();
  }

  isManager(): void {
    this.managesService
      .isManager(this.facilityId)
      .subscribe((state) => (this.isUserManager = state));
  }

  getFacility(): void {
    this.facilityService.findById(this.facilityId).subscribe(
      (facility: Facility) => {
        if (!facility) {
          this.router.navigate(['/error']);
        }
      },
      (error) => {
        this.router.navigate(['/error']);
      }
    );
  }

  loadData(): Promise<void> {
    const usersCountByWeekPromise = this.generateUsersCount(this.facilityId, this.WEEK).toPromise();
    const usersCountByMonthPromise = this.generateUsersCount(this.facilityId, this.MONTH).toPromise();
    const usersCountByYearPromise = this.generateUsersCount(this.facilityId, this.YEAR).toPromise();
    const reviewsCountByWeekPromise = this.generateReviewsCount(this.facilityId, this.WEEK).toPromise();
    const reviewsCountByMonthPromise = this.generateReviewsCount(this.facilityId, this.MONTH).toPromise();
    const reviewsCountByYearPromise = this.generateReviewsCount(this.facilityId, this.YEAR).toPromise();
    const peakHoursByDayPromise = this.generatePeakHoursCount(this.facilityId, this.DAY).toPromise();
    const peakHoursByWeekPromise = this.generatePeakHoursCount(this.facilityId, this.WEEK).toPromise();
    const peakHoursByMonthPromise = this.generatePeakHoursCount(this.facilityId, this.MONTH).toPromise();
    
    return Promise.all
    ([usersCountByWeekPromise, usersCountByMonthPromise, usersCountByYearPromise,
      reviewsCountByWeekPromise, reviewsCountByMonthPromise, reviewsCountByYearPromise,
      peakHoursByDayPromise, peakHoursByWeekPromise, peakHoursByMonthPromise])
      .then
    (([usersCountByWeek, usersCountByMonth, usersCountByYear,
      reviewsCountByWeek, reviewsCountByMonth, reviewsCountByYear,
      peakHoursByDay, peakHoursByWeek, peakHoursByMonth]) => {

      this.usersCountByWeek = usersCountByWeek;
      this.usersCountByMonth = usersCountByMonth;
      this.usersCountByYear = usersCountByYear;
      this.reviewsCountByWeek = reviewsCountByWeek;
      this.reviewsCountByMonth = reviewsCountByMonth;
      this.reviewsCountByYear = reviewsCountByYear;
      this.peakHoursByDay = peakHoursByDay;
      this.peakHoursByWeek = peakHoursByWeek;
      this.peakHoursByMonth = peakHoursByMonth;
    });
  }

  generateUsersCount(facilityId: number, level: string, dateFrom?: string, dateTo?: string): Observable<number> {
    return this.analyticsService.findUsersCountByLevel(facilityId, level, dateFrom, dateTo);
  }

  generateReviewsCount(facilityId: number, level: string, dateFrom?:string, dateTo?:string): Observable<number> {
    return this.analyticsService.findReviewsCountByLevel(facilityId, level, dateFrom, dateTo);
  }

  generatePeakHoursCount(facilityId: number, period: string, dateFrom?: string, dateTo?: string): Observable<any> {
    return this.analyticsService.findPeakHours(facilityId, period, dateFrom, dateTo);
  }

  submitCustomUsersChartForm(): void {
    event?.preventDefault();
    const dateFrom = this.usersCustomDateFrom.nativeElement.value;
    const dateTo = this.usersCustomDateTo.nativeElement.value;
    const usersCountByCustomPromise = this.generateUsersCount(this.facilityId, this.ALL, dateFrom, dateTo).toPromise();
    usersCountByCustomPromise.then((count: any) => {
      this.usersCountByCustomDate = count;
      this.createUsersCustomChart();
    });
  }

  submitCustomReviewsChartForm(): void {
    event?.preventDefault();
    const dateFrom = this.reviewsCustomDateFrom.nativeElement.value;
    const dateTo = this.reviewsCustomDateTo.nativeElement.value;
    const reviewsCountByCustomPromise = this.generateReviewsCount(this.facilityId, this.ALL, dateFrom, dateTo).toPromise();
    reviewsCountByCustomPromise.then((count: any) => {
      this.reviewsCountByCustomDate = count;
      this.createReviewsCustomChart();
    })
  }

  submitCustomPeakHoursChartForm(): void {
    event?.preventDefault();
    const dateFrom = this.peakHoursCustomDateFrom.nativeElement.value;
    const dateTo = this.peakHoursCustomDateTo.nativeElement.value;
    const peakHoursByCustomPromise = this.generatePeakHoursCount(this.facilityId, this.ALL, dateFrom, dateTo).toPromise();
    peakHoursByCustomPromise.then((peakHours: any) => {
      this.peakHoursByCustomDate = peakHours;
      this.createPeakHoursCustomChart();
    })
  }

  createCharts(): void {
    const usersCtx = document.getElementById('usersChart') as HTMLCanvasElement;
    const reviewsCtx = document.getElementById('reviewsChart') as HTMLCanvasElement;
    const peakHoursCtx = document.getElementById('peakHoursChart') as HTMLCanvasElement;

    if (usersCtx) {
      this.createChartElement(usersCtx, ['Week', 'Month', 'Year'], [this.usersCountByWeek!, this.usersCountByMonth!, this.usersCountByYear!], 'Number Of Unique Users');
    }

    if (reviewsCtx) {
      this.createChartElement(reviewsCtx, ['Week', 'Month', 'Year'], [this.reviewsCountByWeek!, this.reviewsCountByMonth!, this.reviewsCountByYear!], 'Number Of Reviews');
    }

    if (peakHoursCtx) {
      const peakHoursData = [
        this.peakHoursByDay!.bestHour.exercises_for_specific_hours_count,
        this.peakHoursByWeek!.bestHour.exercises_for_specific_hours_count,
        this.peakHoursByMonth!.bestHour.exercises_for_specific_hours_count,
        this.peakHoursByDay!.worstHour.exercises_for_specific_hours_count,
        this.peakHoursByWeek!.worstHour.exercises_for_specific_hours_count,
        this.peakHoursByMonth!.worstHour.exercises_for_specific_hours_count
      ];
  
      const peakHoursLabels = [
        this.peakHoursByDay!.bestHour.peak_hour != undefined ? `Best Hour(Day): ${this.peakHoursByDay!.bestHour.peak_hour} h` : "None",
        this.peakHoursByWeek.bestHour.peak_hour != undefined ? `Best Hour(Week): ${this.peakHoursByWeek!.bestHour.peak_hour} h`: "None",
        this.peakHoursByMonth.bestHour.peak_hour != undefined ? `Best Hour(Month): ${this.peakHoursByMonth!.bestHour.peak_hour} h`: "None",
        this.peakHoursByDay!.worstHour.peak_hour != undefined ? `Worst Hour(Day): ${this.peakHoursByDay!.worstHour.peak_hour} h` : "None",
        this.peakHoursByWeek!.worstHour.peak_hour != undefined ? `Worst Hour(Week): ${this.peakHoursByWeek!.worstHour.peak_hour} h` : "None",
        this.peakHoursByMonth!.worstHour.peak_hour != undefined ? `Worst Hour(Month): ${this.peakHoursByMonth!.worstHour.peak_hour} h` : "None"
      ];

    this.createChartElement(peakHoursCtx, peakHoursLabels, peakHoursData, 'Peak Hour Count');
    }

  }

  createUsersCustomChart(): void {
    const usersCustomCtx = document.getElementById('usersCustomChart') as HTMLCanvasElement;
    const existingChart = Chart.getChart(usersCustomCtx);
    if (existingChart) {
      existingChart.destroy();
    }
    if (usersCustomCtx) {
      this.createChartElement(usersCustomCtx, ['Users'], [this.usersCountByCustomDate!], 'Number Of Unique Users');
    }
  }

  createReviewsCustomChart(): void {
    const reviewsCustomCtx = document.getElementById('reviewsCustomChart') as HTMLCanvasElement;
    const existingChart = Chart.getChart(reviewsCustomCtx);
    if (existingChart) {
      existingChart.destroy();
    }
    if (reviewsCustomCtx) {
      this.createChartElement(reviewsCustomCtx, ['Reviews'], [this.reviewsCountByCustomDate!], 'Number Of Reviews');
    }
  }

  createPeakHoursCustomChart(): void {
    const peakHoursCustomCtx = document.getElementById('peakHoursCustomChart') as HTMLCanvasElement;
    const existingChart = Chart.getChart(peakHoursCustomCtx);

    if(existingChart) {
      existingChart?.destroy();
    }
    if(peakHoursCustomCtx) {
      const peakHoursData = [
        this.peakHoursByCustomDate!.bestHour.exercises_for_specific_hours_count,
        this.peakHoursByCustomDate!.worstHour.exercises_for_specific_hours_count
      ];
  
      const peakHoursLabels = [
        this.peakHoursByCustomDate.bestHour.peak_hour != undefined ? `Best: ${this.peakHoursByCustomDate!.bestHour.peak_hour} h`: "None",
         this.peakHoursByCustomDate!.worstHour.peak_hour != undefined ? `Worst: ${this.peakHoursByCustomDate!.worstHour.peak_hour} h` : "None"
      ];

      this.createChartElement(peakHoursCustomCtx, peakHoursLabels, peakHoursData, 'Peak Hour Count');
    }
  }

  destroyCharts(): void {
    this.chartInstances.forEach(chart => chart.destroy());
    this.chartInstances = [];
  }

  createChartElement(
    ctx: HTMLCanvasElement,
    labels: string[],
    data: number[],
    label: string
  ): void {

    const chart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [
          {
            label: label,
            data: data,
            backgroundColor: [
              'rgba(255, 99, 132, 0.2)',
              'rgba(54, 162, 235, 0.2)',
              'rgba(255, 206, 86, 0.2)',
              'rgba(75, 192, 192, 0.2)',
              'rgba(153, 102, 255, 0.2)',
              'rgba(255, 159, 64, 0.2)'
            ],
            borderColor: [
              'rgba(255, 99, 132, 1)',
              'rgba(54, 162, 235, 1)',
              'rgba(255, 206, 86, 1)',
              'rgba(75, 192, 192, 1)',
              'rgba(153, 102, 255, 1)',
              'rgba(255, 159, 64, 1)'
            ],
            borderWidth: 1,
          },
        ],
      },
      options: {
        responsive: true,
        scales: {
          y: {
            beginAtZero: true,
          },
        },
      },
    });

    this.chartInstances.push(chart); // Track the created chart

  }
}
