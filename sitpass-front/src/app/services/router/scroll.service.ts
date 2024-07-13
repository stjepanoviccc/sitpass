import { Injectable } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ScrollService {

  constructor(private router: Router) { }

  scrollToTop() {
    window.scrollTo(0, 0);
  }

  scrollToAnchor(anchor: string): void {
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
      setTimeout(() => {
        const element = document.getElementById(anchor);
        if (element) {
          element.scrollIntoView({ behavior: 'smooth', block: 'start', inline: 'nearest' });
        }
      }, 0);
    });
  }

  subscribeToRouteChanges() {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.scrollToTop();
      }
    });
  }
}
