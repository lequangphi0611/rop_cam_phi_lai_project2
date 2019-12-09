import { map, filter, takeUntil } from 'rxjs/operators';
import { ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject, Subject } from 'rxjs';
import { Component, OnInit, OnDestroy } from '@angular/core';

@Component({
  selector: 'app-statistical',
  templateUrl: './statistical.component.html',
  styleUrls: ['./statistical.component.css']
})
export class StatisticalComponent implements OnInit , OnDestroy{

  tabIndex$ = new BehaviorSubject<number>(0);

  unscription$ = new Subject();

  constructor(private activatedRoute: ActivatedRoute, private route: Router) { }

  ngOnInit() {

    this.activatedRoute.queryParams
      .pipe(
        takeUntil(this.unscription$),
        map(query => query.index),
        filter(index => index != null
      ))
      .subscribe(index => this.tabIndex$.next(index));
  }

  tabIndexChange(index) {
    this.route.navigate([], {
      queryParams: {
        index
      }
    });
  }

  ngOnDestroy(): void {
    this.tabIndex$.complete();
    this.unscription$.next();
    this.unscription$.complete();
  }

}
