/**
 *
 * Copyright (c) 2006-2015, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.speedment.examples.social.util;

import javafx.scene.image.Image;

/**
 *
 * @author Emil Forslund
 */
public final class Avatar {
	private final static String DEFAULT_AVATAR = "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";
	public final static Image DEFAULT_AVATAR_IMG = Base64Util.fromBase64(DEFAULT_AVATAR);
}