package com.primesys.VehicalTracking.Dto;


import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class DriverTaskDayMainDTO extends ExpandableGroup<DriverEmpTaskSheduledDTO> {

  private int iconResId;

  public DriverTaskDayMainDTO(String title, List<DriverEmpTaskSheduledDTO> items, int iconResId) {
    super(title, items);
    this.iconResId = iconResId;
  }

  public int getIconResId() {
    return iconResId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof DriverTaskDayMainDTO)) return false;

    DriverTaskDayMainDTO genre = (DriverTaskDayMainDTO) o;

    return getIconResId() == genre.getIconResId();

  }

  @Override
  public int hashCode() {
    return getIconResId();
  }
}

