package br.com.liferay.uots.portlet;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
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
import com.liferay.util.bridges.mvc.MVCPortlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Portlet implementation class UserOwnThemeScreenPortlet
 */
public class UserOwnThemeScreenPortlet extends MVCPortlet {

	public void savePreference(ActionRequest request, ActionResponse response)
			throws Exception {

		String name = ParamUtil.getString(request, "meu_tema");
		ThemeDisplay themeDisplay = (ThemeDisplay) request
				.getAttribute(WebKeys.THEME_DISPLAY);

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
		long classNameId = table.getClassNameId();
		long columnId = column.getColumnId();
		long classPK = user.getUserId();

		ExpandoValueLocalServiceUtil.addValue(classNameId, table.getTableId(),
				columnId, classPK, name.toLowerCase());

		
		
		ColorScheme color = themeDisplay.getColorScheme();
		color.setCssClass(name.toLowerCase());
		themeDisplay.setLookAndFeel(themeDisplay.getTheme(), color);
		
		sendRedirect(request, response);
	}

	@Override
	public void doView(RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException,
			PortletException {

		String meuTema = "";
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest
					.getAttribute(WebKeys.THEME_DISPLAY);

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
		renderRequest.setAttribute("meu_tema", meuTema);

		super.doView(renderRequest, renderResponse);
	}

}
