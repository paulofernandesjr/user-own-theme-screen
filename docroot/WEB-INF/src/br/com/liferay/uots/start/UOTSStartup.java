package br.com.liferay.uots.start;

import com.liferay.portal.kernel.events.Action;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.ColorScheme;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.expando.DuplicateTableNameException;
import com.liferay.portlet.expando.model.ExpandoColumn;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.model.ExpandoValue;
import com.liferay.portlet.expando.service.ExpandoColumnLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoTableLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoValueLocalServiceUtil;

public class UOTSStartup extends Action {
	
	public UOTSStartup() {
		super();
	}

	public void run(HttpServletRequest request, HttpServletResponse response) throws ActionException {
		
		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		String meuTema = "";
		try {

			// Get a reference to the ExpandoTable (User class)
			ExpandoTable table = null;
			long companyId = themeDisplay.getCompanyId();
			try {
				table = ExpandoTableLocalServiceUtil.addDefaultTable(companyId,
						User.class.getName());
			} catch (DuplicateTableNameException dtne) {
				table = ExpandoTableLocalServiceUtil.getDefaultTable(companyId,
						User.class.getName());
			}

			User user = themeDisplay.getUser();

			ExpandoColumn column = ExpandoColumnLocalServiceUtil.getColumn(
					table.getTableId(), "current-theme");
			long columnId = column.getColumnId();
			long classPK = user.getUserId();

			ExpandoValue value = ExpandoValueLocalServiceUtil.getValue(
					table.getTableId(), columnId, classPK);
			if( value != null )
				meuTema = value.getString();

		} catch (PortalException | SystemException dtne) {

		}
		
		ColorScheme color = themeDisplay.getColorScheme();
		color.setCssClass(meuTema);
		themeDisplay.setLookAndFeel(themeDisplay.getTheme(), color);
		
		
	}

}