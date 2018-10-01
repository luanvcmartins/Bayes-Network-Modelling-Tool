package AIToolkit.Supervisioned.KnowledgeBase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Represents a dataset.
 *
 * @author luan
 */
public class KnowledgeBase<T> {

    private ArrayList<KnowledgeBaseHeader> headers;
    private ArrayList<KnowledgeBaseItem<T>> items;

    private KnowledgeBase() {
    }

    public KnowledgeBase(ArrayList<KnowledgeBaseHeader> headers, ArrayList<KnowledgeBaseItem<T>> items) {
        this.headers = headers;
        this.items = items;
    }

    public KnowledgeBase(ArrayList<KnowledgeBaseItem<T>> items) {
        this.items = items;
    }

    public KnowledgeBaseItem<T> get(int i) {
        return items.get(i);
    }

    public ArrayList<KnowledgeBaseItem<T>> getItems() {
        return items;
    }

    public ArrayList<KnowledgeBaseHeader> getHeaders() {
        return headers;
    }

    /**
     * Returns a builder to be configured with the specifications to find and
     * read the dataset.
     *
     * @return A builder
     */
    public static KnowlegededBaseBuilder builder() {
        return new KnowlegededBaseBuilder();
    }

    public static class KnowlegededBaseBuilder<T> {

        private File file;
        private boolean skipHeader = false;
        private boolean saveHeader = false;
        private String separator = ";";
        private int skipFirstLines = -1;
        private int endAtLine = -1;
        private int maxSample = -1;
        private int exampleName = -1;
        private int className = -1;

        /**
         * Set the location of the file to be read.
         *
         * @param filename The location of the file
         * @return The builder
         */
        public KnowlegededBaseBuilder fromFile(String filename) {
            this.file = new File(filename);
            return this;
        }

        /**
         * Set the instance of the file to be read.
         *
         * @param file The instance of the file
         * @return The builder
         */
        public KnowlegededBaseBuilder fromFile(File file) {
            this.file = file;
            return this;
        }

        /**
         * Set this to true if the first line of the file is the header and must
         * be skipped.
         *
         * @param skip true to skip the first line
         * @return The builder
         */
        public KnowlegededBaseBuilder skipHeader(boolean skip) {
            this.skipHeader = skip;
            return this;
        }

        /**
         * Set the column position of the name or identification of the data.
         *
         * @param columnId The 0-index column id
         * @return The builder
         */
        public KnowlegededBaseBuilder withExampleNameIn(int columnId) {
            this.exampleName = columnId;
            return this;
        }

        /**
         * Set the column index of where the class name of the data is. The
         * default is the last column.
         *
         * @param columnId The colum index.
         * @return The builder
         */
        public KnowlegededBaseBuilder withClassNameIn(int columnId) {
            this.className = columnId;
            return this;
        }

        /**
         * Set the index of the first line to be read.
         *
         * @param startsAtLine Index of where we should start to read.
         * @return The builder
         */
        public KnowlegededBaseBuilder skipFirstNLines(int startsAtLine) {
            this.skipFirstLines = startsAtLine;
            return this;
        }

        /**
         * Set a Max Number of rows to read.
         *
         * @param maxNumber Max number of rows to read.
         * @return The builder
         */
        public KnowlegededBaseBuilder readOnlyNLines(int maxNumber) {
            this.maxSample = maxNumber;
            return this;
        }

        /**
         * Set the CSV file chracter separator. The default is ";".
         *
         * @param separator The character that separates the column of the data.
         * @return The builder
         */
        public KnowlegededBaseBuilder withSeparator(String separator) {
            this.separator = separator;
            return this;
        }

        /**
         * When true the header of the file will be available in the getHeader
         * function.
         *
         * @param saveHeader
         * @return
         */
        public KnowlegededBaseBuilder savingHeader(boolean saveHeader) {
            if (saveHeader) {
                this.skipHeader = true;
            }
            this.saveHeader = saveHeader;
            return this;
        }

        /**
         * Build the Knowledged Base with the given confiiguration. Should be
         * called from a worker thread.
         *
         * @return The KnowledgeBase from the current configuration.
         * @throws Exception
         */
        public KnowledgeBase<T> build() throws Exception {
            if (file == null || !file.exists()) {
                throw new FileNotFoundException("File not found");
            }

            if (maxSample != -1) {
                endAtLine = skipFirstLines + maxSample;
            }

            ArrayList<KnowledgeBaseItem<T>> items = new ArrayList<>();
            ArrayList<KnowledgeBaseHeader> header = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            line = br.readLine();
            if (className == -1) {
                className = line.split(separator).length - 1;
            }
            int lineReaded = 0;
            while (line != null) {
                // 
                if (skipHeader) {
                    skipHeader = false;

                    // User wants to save the header:
                    if (saveHeader) {
                        String[] headerStruct = line.split(separator);

                        // We must do it this way because we don't want to include the 
                        // example name as a attribute:
                        for (int columnId = 0; columnId < headerStruct.length; columnId++) {
                            if (columnId != exampleName && columnId != className) {
                                header.add(new KnowledgeBaseHeader(headerStruct[columnId], header.size()));
                            }
                        }
                    }

                    lineReaded++;
                    line = br.readLine();
                    continue;
                }

                if (skipFirstLines != -1 && lineReaded < skipFirstLines) {
                    lineReaded++;
                    line = br.readLine();
                    continue;
                }
                if (endAtLine != -1 && lineReaded > endAtLine) {
                    break;
                }
                String[] content = line.split(separator);
                KnowledgeBaseItem<T> item = new KnowledgeBaseItem<>();
                ArrayList<T> itemContent = new ArrayList<>();
                for (int columnId = 0; columnId < content.length; columnId++) {
                    if (columnId == exampleName) {
                        item.setName(content[columnId]);
                    } else if (columnId == className) {
                        item.setClassName(content[columnId]);
                    } else {
                        itemContent.add((T) content[columnId]);
                    }
                }
                item.setItems(itemContent);
                items.add(item);
                lineReaded++;
                line = br.readLine();
            }
            if (saveHeader) {
                return new KnowledgeBase<>(header, items);
            }
            return new KnowledgeBase<>(items);
        }
    }
}
