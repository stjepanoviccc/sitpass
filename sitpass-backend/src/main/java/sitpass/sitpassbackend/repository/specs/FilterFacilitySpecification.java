package sitpass.sitpassbackend.repository.specs;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import sitpass.sitpassbackend.model.Discipline;
import sitpass.sitpassbackend.model.Facility;
import sitpass.sitpassbackend.model.WorkDay;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public interface FilterFacilitySpecification {

    static Specification<Facility> withOptionalFields(
            List<String> cities, List<String> disciplines, Float minTotalRating, Float maxTotalRating,
            String workDay, LocalTime from, LocalTime until) {
            return (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();
                Join<Facility, WorkDay> workDayJoin = root.join("workDays");
                Join<Facility, Discipline> disciplineJoin = root.join("disciplines");

                // fetching active always
                predicateList.add(cb.equal(root.get("active"), true));

                if (!cities.isEmpty()) {
                    predicateList.add(root.get("city").in(cities));
                }

                if (!disciplines.isEmpty()) {
                    List<Predicate> disciplinePredicates = new ArrayList<>();
                    for(String discipline : disciplines) {
                        disciplinePredicates.add(cb.like(disciplineJoin.get("name"), "%" + discipline + "%"));
                    }
                    predicateList.add(cb.or(disciplinePredicates.toArray(new Predicate[0])));
                }

                if (minTotalRating != null) {
                    predicateList.add(cb.greaterThanOrEqualTo(root.get("totalRating"), minTotalRating));
                }
                if (maxTotalRating != null) {
                    predicateList.add(cb.lessThanOrEqualTo(root.get("totalRating"), maxTotalRating));
                }

                if (workDay != "") {
                    List<Predicate> workDaysPredicates = new ArrayList<>();
                    workDaysPredicates.add(cb.equal(workDayJoin.get("day"), workDay.toUpperCase()));
                    predicateList.add(cb.or(workDaysPredicates.toArray(new Predicate[0])));

                    // if work day is selected it will check this, otherwise there is no point for checking it.
                    if (from != null || until != null) {
                        Expression<LocalTime> openingTime = workDayJoin.get("from");
                        Expression<LocalTime> closingTime = workDayJoin.get("until");

                        List<Predicate> workHoursPredicates = new ArrayList<>();

                        if (from != null && until != null) {
                            Predicate fromAndUntilPredicate = cb.and(
                                    cb.greaterThanOrEqualTo(openingTime, from),
                                    cb.lessThanOrEqualTo(closingTime, until)
                            );
                            workHoursPredicates.add(fromAndUntilPredicate);
                        }

                        Predicate workHoursPredicate = cb.and(workHoursPredicates.toArray(new Predicate[0]));
                        predicateList.add(workHoursPredicate);
                    }
                }

                return cb.and(predicateList.toArray(new Predicate[0]));
            };
    }

}
