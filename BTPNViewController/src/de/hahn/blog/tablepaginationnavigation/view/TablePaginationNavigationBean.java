package de.hahn.blog.tablepaginationnavigation.view;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.logging.ADFLogger;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.AttributeBinding;
import oracle.binding.BindingContainer;

import org.apache.myfaces.trinidad.event.RangeChangeEvent;

public class TablePaginationNavigationBean {
    private static ADFLogger _logger = ADFLogger.createADFLogger(TablePaginationNavigationBean.class);

    public TablePaginationNavigationBean() {
    }

    public void onGotoPage(ActionEvent actionEvent) {
        BindingContainer bindingContainer = BindingContext.getCurrent().getCurrentBindingsEntry();
        // get number of page to goto
        AttributeBinding attr = (AttributeBinding) bindingContainer.getControlBinding("gotopage1");
        Integer newPage = (Integer) attr.getInputValue();
        if (newPage == null) {
            return;
        }
        // page one starts at index 0 so subtract 1 from the pagen number
        newPage--;

        DCIteratorBinding iter = (DCIteratorBinding) bindingContainer.get("EmployeesView1Iterator");
        // calculate the old and new rages for the RangeChangeEvent
        int range = iter.getRangeSize(); // note both the table and we take the page size from the iterator's RangeSize
        int oldStart = iter.getRangeStart();
        int oldEnd = oldStart + range;
        int newStart = newPage * range;

        int newEnd = newStart + range;
        // find the table
        UIViewRoot iViewRoot = FacesContext.getCurrentInstance().getViewRoot();
        UIComponent table = iViewRoot.findComponent("t1");
        // build the event and fire it
        RangeChangeEvent event = new RangeChangeEvent(table, oldStart, oldEnd, newStart, newEnd);
        table.broadcast(event);
        // update the table
        AdfFacesContext.getCurrentInstance().addPartialTarget(table);
    }

    public void onGetCurrentPage(ActionEvent actionEvent) {
        BindingContainer bindingContainer = BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iter = (DCIteratorBinding) bindingContainer.get("EmployeesView1Iterator");
        int currentRowIndex = iter.getRowSetIterator().getCurrentRowIndex();
        _logger.info("CurrentRowIndex: " + currentRowIndex);
        int currentPage = currentRowIndex / iter.getRangeSize();
        currentPage++;
        _logger.info("Current Page:" + currentPage);
    }
}
