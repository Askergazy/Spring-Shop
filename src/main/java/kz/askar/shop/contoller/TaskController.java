package kz.askar.shop.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping(path = "/task_controller")
public class TaskController {




    @RequestMapping(path = "/city_resource")
    public String citySource(Model model){

        Map<String, Integer> cityToCount = Map.of(
                "Москва", 12_400_000,
                "Киев", 5_700_000,
                "Астана", 1_200_000
        );
        model.addAttribute("cityToCount",cityToCount);

        int sum =0;
        int avg =0;

          if (!cityToCount.isEmpty()){
              for (Map.Entry<String, Integer> city : cityToCount.entrySet()) {
                  sum += city.getValue();
              }
              avg  =  sum / cityToCount.size();
              model.addAttribute("avg",avg);
          }


        return "view/city_source";
    }

}


