/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tika.parser.msexcel;

import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.tika.ms.MSParser;

/**
 * Excel parser
 */
public class MsExcelParser extends MSParser {

    protected String extractText(InputStream input) throws Exception {
        StringBuilder builder = new StringBuilder();
        extractText(new HSSFWorkbook(input), builder);
        return builder.toString();
    }

    private void extractText(HSSFWorkbook book, StringBuilder builder) {
        for (int i = 0; book != null && i < book.getNumberOfSheets(); i++) {
            extractText(book.getSheetAt(i), builder);
        }
    }

    private void extractText(HSSFSheet sheet, StringBuilder builder) {
        for (int i = 0; sheet != null && i <= sheet.getLastRowNum(); i++) {
            extractText(sheet.getRow(i), builder);
        }
    }

    private void extractText(HSSFRow row, StringBuilder builder) {
        for (short i = 0; row != null && i < row.getLastCellNum(); i++) {
            extractText(row.getCell(i), builder);
        }
    }

    private void extractText(HSSFCell cell, StringBuilder builder) {
        if (cell != null) {
            switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                addText(cell.getRichStringCellValue().getString(), builder);
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
            case HSSFCell.CELL_TYPE_FORMULA:
                addText(Double.toString(cell.getNumericCellValue()), builder);
                break;
            default:
                // ignore
            } 
        }
    }

    private void addText(String text, StringBuilder builder) {
        if (text != null) {
            text = text.trim();
            if (text.length() > 0) {
                if (builder.length() > 0) {
                    builder.append(' ');
                }
                builder.append(text);
            }
        }
    }

}
