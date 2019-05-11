create 'user_portrait', {NAME => 'basic_characteristics', VERSIONS => 1}, {NAME => 'nternet_usage_habits', VERSIONS => 1}, {NAME => 'product_usage_habits', VERSIONS => 1}, {NAME => 'other_characteristics', VERSIONS => 1}, {NAME => 'objective', VERSIONS => 1},  {NAME => 'preference', VERSIONS => 1}, {NAME => 'demand', VERSIONS => 1},  {NAME => 'scene', VERSIONS => 1}, {NAME => 'frequency', VERSIONS => 1}


put 'user_portrait', 'u1', 'basic_characteristics:age', 'age'
put 'user_portrait', 'u1', 'basic_characteristics:gender', 'gender'
put 'user_portrait', 'u1', 'basic_characteristics:occupation', 'occupation'
put 'user_portrait', 'u1', 'basic_characteristics:geographical_distribution', 'geographical_distribution'
put 'user_portrait', 'u1', 'basic_characteristics:hobby', 'hobby'

put 'user_portrait', 'u1', 'basic_characteristics:age_level', 'age_level'
put 'user_portrait', 'u1', 'basic_characteristics:pvalue_level', 'pvalue_level'
put 'user_portrait', 'u1', 'basic_characteristics:shopping_level', 'shopping_level'
put 'user_portrait', 'u1', 'basic_characteristics:occupation', 'occupation'
put 'user_portrait', 'u1', 'basic_characteristics:new_user_class_level', 'new_user_class_level'


put 'user_portrait', 'u1', 'internet_usage_habits:internet_access_time', 'internet_access_time'
put 'user_portrait', 'u1', 'internet_usage_habits:length_of_internet_access', 'length_of_internet_access'
put 'user_portrait', 'u1', 'internet_usage_habits:influencing_factors_of_internet_access', 'influencing_factors_of_internet_access'

put 'user_portrait', 'u1', 'product_usage_habits:usage_frequency', 'usage_frequency'
put 'user_portrait', 'u1', 'product_usage_habits:use_time', 'use_time'
put 'user_portrait', 'u1', 'product_usage_habits:duration_of_use', 'duration_of_use'
put 'user_portrait', 'u1', 'product_usage_habits:behavior_characteristics', 'behavior_characteristics'

put 'user_portrait', 'u1', 'product_usage_habits:last_7_day_review', 'last_7_day_review'
put 'user_portrait', 'u1', 'product_usage_habits:last_14_day_review', 'last_14_day_review'
put 'user_portrait', 'u1', 'product_usage_habits:last_7_day_buy', 'last_7_day_buy'
put 'user_portrait', 'u1', 'product_usage_habits:last_14_day_buy', 'last_14_day_buy'


put 'user_portrait', 'u1', 'other_characteristics:understanding_product_channels', 'understanding_product_channels'
put 'user_portrait', 'u1', 'other_characteristics:registration_time', 'registration_time'
put 'user_portrait', 'u1', 'other_characteristics:user_level', 'user_level'
put 'user_portrait', 'u1', 'other_characteristics:activity', 'activity'
put 'user_portrait', 'u1', 'other_characteristics:user_category', 'user_category'


create 'ad_portrait', {NAME => 'basic_characteristics', VERSIONS => 1},  {NAME => 'ad_usage_habits', VERSIONS => 1}, {NAME => 'other_characteristics', VERSIONS => 1}, {NAME => 'objective', VERSIONS => 1},  {NAME => 'preference', VERSIONS => 1}, {NAME => 'demand', VERSIONS => 1},  {NAME => 'scene', VERSIONS => 1}, {NAME => 'frequency', VERSIONS => 1}

put 'ad_portrait', 'a1', 'ad_usage_habits:last_1_day_clicked', 'last_1_day_clicked'
put 'ad_portrait', 'a1', 'ad_usage_habits:last_7_days_clicked', 'last_7_days_clicked'
put 'ad_portrait', 'a1', 'ad_usage_habits:last_1_day_display', 'last_1_day_display'
put 'ad_portrait', 'a1', 'ad_usage_habits:last_7_days_display', 'last_7_days_display'



