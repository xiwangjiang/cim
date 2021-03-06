/**
 * Copyright 2013-2023 Xia Jun(3979434@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package com.farsunset.cim.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.farsunset.cim.api.controller.dto.MessageResult;
import com.farsunset.cim.push.DefaultMessagePusher;
import com.farsunset.cim.push.SystemMessagePusher;
import com.farsunset.cim.sdk.server.model.Message;
import com.farsunset.cim.util.Constants;
import com.farsunset.cim.util.StringUtil;


@RestController
@RequestMapping("/api/message")
public class MessageController  {


	 
	@Autowired
	private SystemMessagePusher systemMessagePusher;
	
	@Autowired
	private DefaultMessagePusher defaultMessagePusher;

	 
	/**
	 * 此方法仅仅在集群时，通过服务器调用
	 * 
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "/dispatch",method=RequestMethod.POST)
	public MessageResult dispatchSend(Message message) {
		return send(message);
	}
	
	
	@RequestMapping(value = "/send",method=RequestMethod.POST)
	public MessageResult send(Message message)  {

		MessageResult result = new MessageResult();

		message.setMid(StringUtil.getUUID());
		if (Constants.MessageType.TYPE_2.equals(message.getAction())) {
			systemMessagePusher.push(message);
		} else {
			defaultMessagePusher.push(message);
		}
		result.id = message.getMid();
		result.timestamp = message.getTimestamp();
		return result;
	}

}
