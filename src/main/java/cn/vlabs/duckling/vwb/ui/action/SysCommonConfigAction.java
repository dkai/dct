/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package cn.vlabs.duckling.vwb.ui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import cn.vlabs.duckling.vwb.KeyConstants;
import cn.vlabs.duckling.vwb.VWBContext;
import cn.vlabs.duckling.vwb.service.resource.Resource;
import cn.vlabs.duckling.vwb.ui.base.BaseAction;
import cn.vlabs.duckling.vwb.ui.command.VWBCommand;
import cn.vlabs.duckling.vwb.ui.form.SysCommonConfigActionForm;

public class SysCommonConfigAction extends BaseAction {

	Logger log = Logger.getLogger(SysCommonConfigAction.class);
	
	public static final String oriApplicationName = "协同工作环境套件";

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		SysCommonConfigActionForm scf = (SysCommonConfigActionForm) form;
		// 返回自身页面
		//
		Resource res = this.getSavedViewPort(request);
		VWBContext context = VWBContext.createContext(request,
				VWBCommand.ADMIN, res);
		if (context.hasAccess(response)) {

			try {
				String oldAppName = context.getSiteName();
				
				String act = scf.getAct();
				if ("set".equals(act)) {
					String appName = scf.getApplicationName();
					if (appName == null) {
						appName = "";
					}
					VWBContext.getContainer().getSiteConfig().setProperty(context.getSiteId(), KeyConstants.SITE_NAME_KEY,
							appName);
				} else {
					if(oldAppName==null || oldAppName.equals(""))
						scf.setApplicationName(oriApplicationName);
					scf.setApplicationName(oldAppName);
				}
				request.setAttribute("SysCommonConfigForm", scf);
			} catch (Exception e) {
				request.setAttribute("error",
						"SysCommonConfigAction.executeFail");
			}
			return doLayout(context);
		}
		return null;
	}

	private ActionForward doLayout(VWBContext context) {
		return layout(context, "/jsp/sysCommonConfig.jsp");
	}

}
