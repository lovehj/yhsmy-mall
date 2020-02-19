package com.yhsmy.entity.vo.act;

import com.yhsmy.enums.ApproveEnum;
import com.yhsmy.enums.NormalEnum;
import com.yhsmy.utils.DateTimeUtil;

/**
 * @auth 李正义
 * @date 2019/12/20 17:20
 **/
public class Leave extends com.yhsmy.entity.act.Leave {
    private static final long serialVersionUID = -598582555613625067L;

    public String getStateStr() {
        return NormalEnum.getValueByKey (super.getState ());
    }

    public String getCreateTimeStr() {
        return DateTimeUtil.localDateTimeToStr (super.getCreateTime ());
    }

    public String getStartDateStr() {
        return DateTimeUtil.localDateTimeToStr (super.getStartDate ());
    }

    public String getEndDateStr() {
        return DateTimeUtil.localDateTimeToStr (super.getEndDate ());
    }

    public String getDaysStr() {
        String days = super.getDays ();
        int splitLen = days.indexOf (".");
        if(splitLen > 0) {
            String startDay = days.substring (0, splitLen),
                    endDay = days.substring (days.indexOf (".")+1, days.length ());
            try{
                if(Integer.parseInt (endDay) > 0) {
                    return startDay+"天半";
                }
            }catch (Exception e) { }
            return startDay+"天";
        }
        return days+"天";
    }

    public String getStartKey() {
        return ApproveEnum.LEAVEBILL.getKey ();
    }
}
