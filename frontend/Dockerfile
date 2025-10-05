FROM node:20-alpine

# Set working directory
WORKDIR /app

# Install dependencies
COPY package*.json ./
RUN npm install

# Copy the rest of the source code
COPY . .

# Expose the dev server port
EXPOSE 3000

# Set environment variable for Vite dev server
ENV PORT=3000
ENV HOST=0.0.0.0

# Start Vite in development mode
CMD ["npm", "run", "preview", "--", "--port", "3000", "--host"]
