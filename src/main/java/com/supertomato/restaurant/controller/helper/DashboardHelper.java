package com.supertomato.restaurant.controller.helper;

import com.supertomato.restaurant.common.enums.ProductType;
import com.supertomato.restaurant.common.util.Constant;
import com.supertomato.restaurant.common.util.DateUtil;
import com.supertomato.restaurant.controller.model.response.AverageNumber;
import com.supertomato.restaurant.controller.model.response.HistoryOfDeviation;
import com.supertomato.restaurant.controller.model.response.Served;
import com.supertomato.restaurant.controller.model.response.TotalServedResponse;
import com.supertomato.restaurant.entity.Ingredient;
import com.supertomato.restaurant.entity.IngredientHistory;
import com.supertomato.restaurant.entity.Outlet;
import com.supertomato.restaurant.entity.ProductHistory;
import com.supertomato.restaurant.service.ProductHistoryService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DashboardHelper {

    public TotalServedResponse getServers(Date from, Date to, ProductHistoryService productHistoryService, Outlet outlet, String sortField, boolean isAsc) {

        List<Served> servedList = new ArrayList<>();
        Date date = DateUtil.setDate(from, 30);
        Date endDate = to;
        if (date.before(to)) {
            if (date.after(from)) {
                endDate = date;
            } else {
                endDate = DateUtil.setDate(from, 59);
            }
        }
        servedList.add(new Served(from, endDate, 0, 0, 0, 0, 0));
        if (endDate.before(to)) {
            do {
                Date sDate = DateUtil.addSecond(endDate, 1);
                Date end = DateUtil.addMinutes(endDate, 30);
                endDate = to;
                if (end.before(to)) {
                    endDate = end;
                }
                servedList.add(new Served(sDate, endDate, 0, 0, 0, 0, 0));
            } while (endDate.before(to));
        }

        long totalBowl = 0;
        long totalKebab = 0;
        long totalQuesadilla = 0;
        long totalBurrito = 0;
        long totalServed = 0;
        List<ProductHistory> productHistoryList = productHistoryService.getAllByOutletIdAndStartTimeBetween(outlet.getId(), from, to);

        if (!productHistoryList.isEmpty()) {
            for (Served served : servedList) {
                List<ProductHistory> productHistories = productHistoryList.stream().filter(productHistory -> productHistory.getEndTime() != null &&
                        productHistory.getEndTime().getTime() >= served.getStartTime().getTime()
                        && productHistory.getEndTime().getTime() <= served.getStartTime().getTime()).collect(Collectors.toList());
                if (!productHistories.isEmpty()) {
                    served.setBowl(productHistories.stream().filter(productHistory -> ProductType.DAILY_BOWL.equals(productHistory.getProductType())).count());
                    served.setBurrito(productHistories.stream().filter(productHistory -> ProductType.BURRITO.equals(productHistory.getProductType())).count());
                    served.setQuesadilla(productHistories.stream().filter(productHistory -> ProductType.QUESADILLA.equals(productHistory.getProductType())).count());
                    served.setKebab(productHistories.stream().filter(productHistory -> ProductType.KEBAB.equals(productHistory.getProductType())).count());
                    served.setTotal(productHistories.size());
                }
            }
            totalBowl = servedList.stream().mapToLong(Served::getBowl).sum();
            totalKebab = servedList.stream().mapToLong(Served::getKebab).sum();
            totalQuesadilla = servedList.stream().mapToLong(Served::getQuesadilla).sum();
            totalBurrito = servedList.stream().mapToLong(Served::getBurrito).sum();
            totalServed = servedList.stream().mapToLong(Served::getTotal).sum();

        }
        switch (sortField) {
            case Constant.SORT_BY_TOTAL:
                if (isAsc) {
                    servedList = servedList.stream().sorted(Comparator.comparingLong(Served::getTotal)).collect(Collectors.toList());
                } else {
                    servedList = servedList.stream().sorted(Comparator.comparingLong(Served::getTotal).reversed()).collect(Collectors.toList());
                }
                break;
            case Constant.SORT_BY_BURRITO:
                if (isAsc) {
                    servedList = servedList.stream().sorted(Comparator.comparingLong(Served::getBurrito)).collect(Collectors.toList());
                } else {
                    servedList = servedList.stream().sorted(Comparator.comparingLong(Served::getBurrito).reversed()).collect(Collectors.toList());
                }
                break;
            case Constant.SORT_BY_QUESADILLA:
                if (isAsc) {
                    servedList = servedList.stream().sorted(Comparator.comparingLong(Served::getQuesadilla)).collect(Collectors.toList());
                } else {
                    servedList = servedList.stream().sorted(Comparator.comparingLong(Served::getQuesadilla).reversed()).collect(Collectors.toList());
                }
                break;
            case Constant.SORT_BY_KEBAB:
                if (isAsc) {
                    servedList = servedList.stream().sorted(Comparator.comparingLong(Served::getKebab)).collect(Collectors.toList());
                } else {
                    servedList = servedList.stream().sorted(Comparator.comparingLong(Served::getKebab).reversed()).collect(Collectors.toList());
                }
                break;
            case Constant.SORT_BY_BOWL:
                if (isAsc) {
                    servedList = servedList.stream().sorted(Comparator.comparingLong(Served::getBowl)).collect(Collectors.toList());
                } else {
                    servedList = servedList.stream().sorted(Comparator.comparingLong(Served::getBowl).reversed()).collect(Collectors.toList());
                }
                break;
            default:
                if (isAsc) {
                    servedList = servedList.stream().sorted(Comparator.comparing(Served::getStartTime).reversed()).collect(Collectors.toList());
                } else {
                    servedList = servedList.stream().sorted(Comparator.comparing(Served::getStartTime)).collect(Collectors.toList());
                }
        }

        return new TotalServedResponse(totalBowl, totalKebab, totalQuesadilla, totalBurrito, totalServed, servedList);
    }

    public List<HistoryOfDeviation> getHistoryOfDeviation(String sortField, boolean isAsc,
                                                          List<Ingredient> ingredientList,
                                                          List<IngredientHistory> ingredientHistoryList) {

        List<HistoryOfDeviation> historyOfDeviationList = new ArrayList<>();

        for (Ingredient ingredient : ingredientList) {
            List<IngredientHistory> ingredientHistories = ingredientHistoryList
                    .stream().filter(ingredientHistory -> ingredientHistory.getIngredientId().equals(ingredient.getId()))
                    .sorted(Comparator.comparing(IngredientHistory::getCreatedDate).reversed()).limit(10)
                    .collect(Collectors.toList());
            double portion = 100;
            if (!ingredientHistories.isEmpty()) {
                double sumPortion = ingredientHistories.stream().mapToDouble(IngredientHistory::getPortion).sum() / ingredientHistories.size();
                portion = (sumPortion / ingredient.getPortion()) * 100;
            }
            historyOfDeviationList.add(new HistoryOfDeviation(ingredient, 40, 3000, portion));
        }
        if (!historyOfDeviationList.isEmpty()) {
            switch (sortField) {
                case Constant.SORT_BY_NAME:
                    if (isAsc) {
                        historyOfDeviationList = historyOfDeviationList.stream().sorted(Comparator.comparing(HistoryOfDeviation::getName)).collect(Collectors.toList());
                    } else {
                        historyOfDeviationList = historyOfDeviationList.stream().sorted(Comparator.comparing(HistoryOfDeviation::getName).reversed()).collect(Collectors.toList());
                    }
                    break;
                case Constant.SORT_BY_TOTAL_PORTION:
                    if (isAsc) {
                        historyOfDeviationList = historyOfDeviationList.stream().sorted(Comparator.comparingDouble(HistoryOfDeviation::getTotalPortion)).collect(Collectors.toList());
                    } else {
                        historyOfDeviationList = historyOfDeviationList.stream().sorted(Comparator.comparingDouble(HistoryOfDeviation::getTotalPortion).reversed()).collect(Collectors.toList());
                    }
                    break;
                case Constant.SORT_BY_TOTAL_VOLUME:
                    if (isAsc) {
                        historyOfDeviationList = historyOfDeviationList.stream().sorted(Comparator.comparingDouble(HistoryOfDeviation::getTotalVolume)).collect(Collectors.toList());
                    } else {
                        historyOfDeviationList = historyOfDeviationList.stream().sorted(Comparator.comparingDouble(HistoryOfDeviation::getTotalVolume).reversed()).collect(Collectors.toList());
                    }
                    break;
                case Constant.SORT_BY_PORTION:
                    if (isAsc) {
                        historyOfDeviationList = historyOfDeviationList.stream().sorted(Comparator.comparingDouble(HistoryOfDeviation::getPortion)).collect(Collectors.toList());
                    } else {
                        historyOfDeviationList = historyOfDeviationList.stream().sorted(Comparator.comparingDouble(HistoryOfDeviation::getPortion).reversed()).collect(Collectors.toList());
                    }
                    break;
                case Constant.SORT_BY_ACCEPTABLE_DEVIATION:
                    if (isAsc) {
                        historyOfDeviationList = historyOfDeviationList.stream().sorted(Comparator.comparingDouble(HistoryOfDeviation::getAcceptableDeviation)).collect(Collectors.toList());
                    } else {
                        historyOfDeviationList = historyOfDeviationList.stream().sorted(Comparator.comparingDouble(HistoryOfDeviation::getAcceptableDeviation).reversed()).collect(Collectors.toList());
                    }
                    break;
                default:
                    if (isAsc) {
                        historyOfDeviationList = historyOfDeviationList.stream().sorted(Comparator.comparingDouble(HistoryOfDeviation::getAcceptable)).collect(Collectors.toList());
                    } else {
                        historyOfDeviationList = historyOfDeviationList.stream().sorted(Comparator.comparingDouble(HistoryOfDeviation::getAcceptable).reversed()).collect(Collectors.toList());
                    }
            }
        }

        return historyOfDeviationList;
    }

    public AverageNumber getAverageNumber(Outlet outlet, Date from, Date to, ProductHistoryService productHistoryService) {
        long fulfilmentTime = 0;
        long waitingTime = 0;
        int deviation = 0;
        int numberOrder = 0;
        List<ProductHistory> productHistoryList = productHistoryService.getAllByOutletIdAndStartTimeBetween(outlet.getId(), from, to);
        if (!productHistoryList.isEmpty()) {
            waitingTime = productHistoryList.stream().mapToLong(ProductHistory::getWaitingTime).sum() / productHistoryList.size();
            long time = 0;
            for (ProductHistory productHistory : productHistoryList) {
                if (productHistory.getEndTime() != null && productHistory.getStartTime() != null) {
                    time += productHistory.getEndTime().getTime() - productHistory.getStartTime().getTime();
                }
            }
            fulfilmentTime = time / productHistoryList.size();
        }

        return new AverageNumber(90000, 66000, 20, 2);
    }

}
