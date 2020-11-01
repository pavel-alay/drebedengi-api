package com.alay.drebedengi.soap.functions;

import com.alay.drebedengi.api.WebClient;
import com.alay.drebedengi.soap.functions.base.MapWithIdListFunction;
import com.alay.drebedengi.soap.responses.GetRecordListReturn;

import java.io.IOException;

/**
 * Retrievs record list (array of arrays) or report table by parameters;
 * [params] => array of following parameters:
 * 'is_report' [true|false (no default)] - retrievs data for report only or full records (waste, incomes, moves, changes) for export;
 * 'relative_date' [YYYY-MM-DD (NOW by default)] - all data will be retrieved relative to this value, according to 'r_period' value;
 * 'period_to', 'period_from' [YYYY-MM-DD] - custom period, if 'r_period' = 0;
 * 'is_show_duty' [true(default)|false] - whether or not include duty record;
 * 'r_period' [custom period = 0, this month = 1, today = 7, last month = 2, this quart = 3, this year = 4, last year = 5, all time = 6, last 20 record = 8 (default)] - period for which data will be obtained;
 * 'r_what' [income = 2, waste = 3 (default), move = 4, change = 5, all types = 6] - type of data you want to get;
 * 'r_who' [0 (default) - all users, int8 = user ID] - The data of the user to obtain, in the case of multiplayer mode;
 * 'r_how' [show record list by detail = 1 (default), group incomes by source = 2, group wastes by category = 3] - Values 2 and 3 are for 'report' mode only# How to group the result record list;
 * 'r_middle' [No average = 0 (default), Average monthly = 2592000, Average weekly = 604800, Averaged over days = 86400] - How to average the data, if r_how = 2 or 3;
 * 'r_currency' [Original currency = 0 (default), int8 = currency ID] - Convert or not in to given currency;
 * 'r_is_place', 'r_is_tag', 'r_is_category' [Include all = 0 (default), Include only selected = 1, All except selected = 2] - Exclude or include 'r_place', 'r_tag' or 'r_category' respectively; 'r_place', 'r_tag', 'r_category' [Array] - Array of numeric values for place ID, tag ID or category ID respectively; If parameter [idList] is given, it will be treat as ID list of objects to retrieve# this is used for synchronization;
 */
public class GetRecordList extends MapWithIdListFunction<GetRecordList, GetRecordListReturn> {
    @Override
    public GetRecordListReturn request(WebClient client) throws IOException {
        return new GetRecordListReturn().init(sendRequest(client));
    }
}
