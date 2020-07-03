package util;

import java.util.LinkedList;
import java.util.List;

import javaBean.Order;
import javaBean.PickingTask;

public interface TaskGenerator
{
	LinkedList<PickingTask> generateTask(List<Order> orders);
}
